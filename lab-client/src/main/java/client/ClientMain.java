package client;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.Request;
import ui.Response;

import java.io.IOException;
import java.util.Scanner;

@RequiredArgsConstructor
public class ClientMain {
    private static final Logger logger = LogManager.getLogger(ClientMain.class);
    private final ConnectionUDP connectionUDP;


    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java ClientMain <host> <port>");
            System.exit(1);
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        try {
            ClientMain clientMain = new ClientMain(new ConnectionUDP(host, port));
            logger.info("Client started. Connected to {}:{}", host, port);
            clientMain.start();
        } catch (IOException e) {
            logger.error("Failed to start client: {}", e.getMessage());
            System.exit(1);
        }
    }

    public void start() {

        try (Scanner scanner = new Scanner(System.in)) {
            RequestManager requestManager = new RequestManager(connectionUDP);

            while (connectionUDP.isConnected()) {

                System.out.print("> ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;

                try {
                    Request request = requestManager.generateRequest(input, scanner);

                    Response response = connectionUDP.sendRequestWithRetry(request);

                    System.out.println(response);
                } catch (Exception e) {
//                    logger.error("Error processing command: {}", e.getMessage());
                    System.out.println("Введите команду help для получения списка команд!");
                    if (!connectionUDP.checkConnection()) {
                        connectionUDP.handleDisconnection();
                    }
                }
            }
        } finally {
            connectionUDP.closeChannel();
            connectionUDP.getHeartbeatExecutor().shutdown();
        }
    }
}