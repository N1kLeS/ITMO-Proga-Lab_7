package client;

import ClientCommands.ExitClientCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.*;
import utils.ElementInputHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClientMain {
    private static final Logger logger = LogManager.getLogger(ClientMain.class);
    private static final int BUFFER_SIZE = 65507;
    private static final int CONNECTION_ATTEMPTS = 3;
    private static final int CONNECTION_TIMEOUT_MS = 2000;
    private static final int RESPONSE_TIMEOUT_SEC = 5;
    private static final int HEARTBEAT_INTERVAL_SEC = 5;
    private static final int RECONNECT_ATTEMPTS = 3;

    private final String host;
    private final int port;
    private final DatagramChannel channel;
    private volatile boolean isConnected = false;
    private final ScheduledExecutorService heartbeatExecutor = Executors.newSingleThreadScheduledExecutor();
    private final HashMap<String, CommandInfo> serverCommandsInfoList;
    private final CommandHandler clientCommandHandler;

    public ClientMain(String host, int port) throws IOException {
        this.host = host;
        this.port = port;
        this.channel = DatagramChannel.open();
        this.channel.configureBlocking(false);
        this.serverCommandsInfoList = new HashMap<>();
        this.clientCommandHandler = new CommandHandler();

        registerClientCommands();

        connectToServer();
    }

    private void registerClientCommands() {
        clientCommandHandler.register(new ExitClientCommand());
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

    private void handleDisconnection() {
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

    private void loadCommandsList() {

        Response response = sendRequestWithRetry(new Request("help", new String[0]));

        for (CommandInfo commandInfo : (ArrayList<CommandInfo>) response.getData())
            this.serverCommandsInfoList.put(commandInfo.getName(), commandInfo);

    }

    public void start() {

        try (Scanner scanner = new Scanner(System.in)) {
            logger.info("Client started. Connected to {}:{}", host, port);

            loadCommandsList();

            while (isConnected) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;

                try {
                    String[] parts = input.split(" ", 2);
                    String commandName = parts[0];
                    String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];


                    if (!serverCommandsInfoList.containsKey(commandName)) {
                        System.out.println("Неизвестная команда: " + commandName);
                        continue;
                    }

                    logger.info("Processing command: {}", commandName);

                    CommandInfo command = serverCommandsInfoList.get(commandName);
                    CommandType commandType = command.getCommandType();
                    if (commandType.getArgumentCount() != args.length) {
                        System.out.println("Неверное количество аргументов для команды: " + commandName);
                        continue;
                    }

                    if (!command.isServerCommand()) {
                        Command clientCommand = clientCommandHandler.getCommand(commandName);
                        if (clientCommand != null) {
                            clientCommand.execute(new Request(commandName, args));
                        } else {
                            System.out.println("Команда не найдена: " + commandName);
                            continue;
                        }
                    }

                    Response response;
                    Object object = null;

                    if (commandType.hasForm()) {
                        ElementInputHandler inputHandler = new ElementInputHandler();
                        object = inputHandler.readValue(commandType.getFormClass());
                    }

                    response = sendRequestWithRetry(new Request(commandName, args, object));

                    handleResponse(response);
                } catch (Exception e) {
                    logger.error("Error processing command: {}", e.getMessage());
                    if (!checkConnection()) {
                        handleDisconnection();
                    }
                }
            }
        } finally {
            closeChannel();
            heartbeatExecutor.shutdown();
        }
    }

    private boolean checkConnection() {
        try {
            Response response = sendRequestWithRetry(new Request("ping", new String[0]));
            return response != null && response.getMessage().equals("pong");
        } catch (Exception e) {
            return false;
        }
    }

    private Response sendRequestWithRetry(Request request) {
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


    private void handleResponse(Response response) {
        if (response != null) {
            System.out.println("Server response: " + response);
        } else {
            System.out.println("No response from server.");
        }
    }

    private void sendRequest(Request request) throws IOException {
        byte[] requestData = Serialization.serialize(request);
        ByteBuffer buffer = ByteBuffer.wrap(requestData);
        channel.send(buffer, new InetSocketAddress(host, port));
        logger.debug("Sent request: {}", request);
    }

    private void closeChannel() {
        try {
            if (channel != null && channel.isOpen()) {
                channel.close();
            }
        } catch (IOException e) {
            logger.error("Failed to close channel: {}", e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java ClientMain <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            new ClientMain(host, port).start();
        } catch (IOException e) {
            logger.error("Failed to start client: {}", e.getMessage());
            System.exit(1);
        }
    }
}