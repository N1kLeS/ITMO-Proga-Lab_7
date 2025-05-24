package client;

import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.Request;
import ui.Response;
import ui.Serialization;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionUDP {
    private static final Logger logger = LogManager.getLogger(ConnectionUDP.class);

    private static final int BUFFER_SIZE = 65507;
    private static final int CONNECTION_ATTEMPTS = 3;
    private static final int CONNECTION_TIMEOUT_MS = 2000;
    private static final int RESPONSE_TIMEOUT_SEC = 5;
    private static final int HEARTBEAT_INTERVAL_SEC = 5;
    private static final int RECONNECT_ATTEMPTS = 3;

    private final String host;
    private final int port;
    private final DatagramChannel channel;
    @Getter
    private volatile boolean isConnected = false;
    @Getter
    private final ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();

    public ConnectionUDP(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.channel = DatagramChannel.open();
        this.channel.configureBlocking(false);

        connectToServer();
    }


    private void connectToServer() throws IOException {
        logger.info("Connecting to server...");
        for (int attempt = 1; attempt <= CONNECTION_ATTEMPTS; attempt++) {
            try {
                checkServerConnection();
                isConnected = true;
                startHeartbeat();
                return;
            } catch (IOException e) {
                logger.warn("Connection attempt {}/{} failed", attempt, CONNECTION_ATTEMPTS);
                if (attempt == CONNECTION_ATTEMPTS) {
                    closeChannel();
                    throw e;
                }
            }
        }
    }

    private void checkServerConnection() throws IOException {
        Request pingRequest = new Request("ping", new String[0]);
        Instant startTime = Instant.now();

        for (int attempt = 1; attempt <= CONNECTION_ATTEMPTS; attempt++) {
            try {
                sendRequest(pingRequest);
                Response response = waitForResponse();
                if (response != null && response.getMessage().equals("pong")) {
                    logger.info("Successfully connected to server!");
                    return;
                }
            } catch (IOException e) {
                logger.warn("Connection check attempt {}/{} failed: {}", attempt, CONNECTION_ATTEMPTS, e.getMessage());
            }

            if (Duration.between(startTime, Instant.now()).getSeconds() > RESPONSE_TIMEOUT_SEC) {
                break;
            }

            try {
                Thread.sleep(CONNECTION_TIMEOUT_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        throw new IOException("Failed to connect to server");
    }

    private void startHeartbeat() {
        heartbeatExecutor.scheduleAtFixedRate(() -> {
            if (!isConnected) return;

            try {
                Response response = sendRequestWithRetry(new Request("ping", new String[0]));
                if (response == null || !response.getMessage().equals("pong")) {
                    handleDisconnection();
                }
            } catch (Exception e) {
                handleDisconnection();
            }
        }, HEARTBEAT_INTERVAL_SEC, HEARTBEAT_INTERVAL_SEC, TimeUnit.SECONDS);
    }

    public void handleDisconnection() {
        if (!isConnected) return;

        logger.warn("Connection lost! Attempting to reconnect...");
        isConnected = false;
        heartbeatExecutor.shutdown();

        for (int attempt = 1; attempt <= RECONNECT_ATTEMPTS; attempt++) {
            try {
                connectToServer();
                logger.info("Reconnected successfully!");
                return;
            } catch (IOException e) {
                logger.warn("Reconnect attempt {}/{} failed", attempt, RECONNECT_ATTEMPTS);
            }
        }

        logger.error("Failed to reconnect after {} attempts. Shutting down.", RECONNECT_ATTEMPTS);
        closeChannel();
        System.exit(1);
    }


    public boolean checkConnection() {
        try {
            Response response = sendRequestWithRetry(new Request("ping", new String[0]));
            return response != null && response.getMessage().equals("pong");
        } catch (Exception e) {
            return false;
        }
    }

    public Response sendRequestWithRetry(Request request) {
        try {
            sendRequest(request);
            return waitForResponse();
        } catch (IOException e) {
            logger.error("Request failed: {}", e.getMessage());
            return Response.error("Connection error: " + e.getMessage());
        }
    }

    private Response waitForResponse() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        Instant start = Instant.now();

        while (Duration.between(start, Instant.now()).getSeconds() < RESPONSE_TIMEOUT_SEC) {
            try {
                InetSocketAddress sender = (InetSocketAddress) channel.receive(buffer);
                if (sender != null) {
                    buffer.flip();
                    byte[] responseData = new byte[buffer.remaining()];
                    buffer.get(responseData);
                    return Serialization.deserialize(responseData);
                }
                Thread.sleep(50);
            } catch (SocketTimeoutException | InterruptedException e) {
                throw new IOException("Response timeout");
            } catch (ClassNotFoundException e) {
                throw new IOException("Deserialization error");
            }
        }
        throw new IOException("Response timeout");
    }


    private void sendRequest(Request request) throws IOException {
        byte[] requestData = Serialization.serialize(request);
        ByteBuffer buffer = ByteBuffer.wrap(requestData);
        channel.send(buffer, new InetSocketAddress(host, port));
        logger.debug("Sent request: {}", request);
    }

    public void closeChannel() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        } catch (IOException e) {
            logger.error("Failed to close channel: {}", e.getMessage());
        }
    }
}
