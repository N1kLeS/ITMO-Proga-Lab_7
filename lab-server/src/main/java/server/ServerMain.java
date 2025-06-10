package server;

import DataBase.DBConnector;
import DataBase.TicketDAO;
import DataBase.UserDAO;
import authentication.User;
import commands.auth.LoginCommand;
import commands.auth.RegistrationCommand;
import commands.data.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.CollectionManager;
import service.UserService;
import ui.CommandHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ServerMain {
    private static final Logger logger = LogManager.getLogger(ServerMain.class);
    private static final int BUFFER_SIZE = 65507;

    private final int port;
    private final CollectionManager collectionManager;
    private final CommandHandler commandHandler;
    private final UserService userService;
    private final DBConnector dbConnector;
    private final ExecutorService requestProcessorPool;
    private final ForkJoinPool responsePool;

    private volatile boolean isRunning = true;

    public ServerMain(int port, String URL, String dbUser, String dbPassword) throws SQLException {
        this.port = port;
        this.dbConnector = new DBConnector(URL, dbUser, dbPassword);
        this.collectionManager = new CollectionManager(new TicketDAO(dbConnector.getConnection()));
        this.commandHandler = new CommandHandler();
        this.userService = new UserService(new UserDAO(dbConnector.getConnection()));
        this.requestProcessorPool = Executors.newCachedThreadPool();
        this.responsePool = new ForkJoinPool();

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
        commandHandler.register(new RegistrationCommand(userService));
        commandHandler.register(new LoginCommand(userService));
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
        }
    }

    private void processRequests(DatagramChannel channel, ByteBuffer buffer) throws IOException {
        InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);
        if (clientAddress != null) {
            buffer.flip();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            buffer.clear();

            requestProcessorPool.submit(() -> {
                try {
                    RequestProcessor processor = new RequestProcessor(channel, clientAddress, data, commandHandler, userService, responsePool);
                    processor.process();
                } catch (Exception e) {
                    logger.error("Error processing request: {}", e.getMessage());
                }
            });
        }
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.err.println("Usage: java ServerMain <port> <dbURL> <dbUser> <dbPassword>");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        String dbURL = args[1];
        String dbUser = args[2];
        String dbPassword = args[3];

        try {
            ServerMain server = new ServerMain(port, dbURL, dbUser, dbPassword);
            Runtime.getRuntime().addShutdownHook(new Thread(server::shutdown));

            server.start();
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    public void shutdown() {
        isRunning = false;
        requestProcessorPool.shutdown();
        responsePool.shutdown();
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

                if (commandName.equals("exit") && args.length == 0) {

                    System.out.println("Выключение сервера.");
                    logger.info("Сервер выключен.");
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
