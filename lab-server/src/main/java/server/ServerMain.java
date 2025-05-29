package server;

import commands.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.CollectionManager;
import ui.CommandHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Scanner;

public class ServerMain {
    private static final Logger logger = LogManager.getLogger(ServerMain.class);
    private static final int BUFFER_SIZE = 65507;

    private final int port;
    private final CollectionManager collectionManager;
    private final CommandHandler commandHandler;
    private volatile boolean isRunning = true;

    public ServerMain(int port, String dataFile) {
        this.port = port;
        this.collectionManager = new CollectionManager(dataFile);
        this.commandHandler = new CommandHandler();
        initializeCommands();
    }

    private void initializeCommands() {
        commandHandler.register(new AddCommand(collectionManager));
        commandHandler.register(new InfoCommand(collectionManager));
        commandHandler.register(new ShowCommand(collectionManager));
        commandHandler.register(new UpdateCommand(collectionManager));
        commandHandler.register(new ClearCommand(collectionManager));
        commandHandler.register(new RemoveByIdCommand(collectionManager));
        commandHandler.register(new AddIfMinCommand(collectionManager));
        commandHandler.register(new RemoveGreaterCommand(collectionManager));
        commandHandler.register(new RemoveLowerCommand(collectionManager));
        commandHandler.register(new CountGreaterThanTypeCommand(collectionManager));
        commandHandler.register(new FilterByRefundableCommand(collectionManager));
        commandHandler.register(new FilterGreaterThanPersonCommand(collectionManager));
        commandHandler.register(new HelpCommand(commandHandler));
        commandHandler.register(new ExitClientCommand());
        commandHandler.register(new ExecuteScriptCommand());
    }

    public void start() {
        logger.info("Запускаем сервер на порту {}", port);

        Thread consoleThread = new Thread(this::serverConsole);
        consoleThread.setDaemon(true);
        consoleThread.start();

        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.bind(new InetSocketAddress(port));
            channel.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (isRunning) {
                processRequests(channel, buffer);
                Thread.sleep(50);
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Ошибка сервера: {}", e.getMessage());
        } finally {
            collectionManager.saveCollection();
        }
    }

    private void processRequests(DatagramChannel channel, ByteBuffer buffer) throws IOException {
        InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);
        if (clientAddress != null) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            buffer.clear();

            new RequestProcessor(channel, clientAddress, data, commandHandler).start();
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: java ServerMain <port> <data_file>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        String dataFile = args[1];

        ServerMain server = new ServerMain(port, dataFile);
        Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));

        server.start();
    }

    private void shutdown() {
        isRunning = false;
        if (collectionManager.saveCollection()) {
            logger.info("Коллекция успешно сохранена.");
        } else {
            logger.error("Ошибка при сохранении коллекции.");
        }
        logger.info("Server shutdown initiated");
    }

    private void serverConsole() {
        Scanner scanner = new Scanner(System.in);

        while (isRunning) {
            try {
                String input = scanner.nextLine().trim();

                String[] parts = input.split(" ", 2);
                String commandName = parts[0];
                String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];

                if (commandName.equals("save") && args.length == 0) {
                    collectionManager.saveCollection();
                    System.out.println("Коллекция успешно сохранена.");
                    logger.info("Коллекция успешно сохранена.");
                } else {
                    System.out.println("Неизвестная команда: " + commandName);
                }
                Thread.sleep(50);
            } catch (Exception e) {
                logger.error("Ошибка при выполнении команды: " + e.getMessage());
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }
}
