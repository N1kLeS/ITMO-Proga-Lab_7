//package server;
//
//import controller.CommandHandler;
//import controller.commands.*;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import service.CollectionManager;
//
//import java.io.IOException;
//
//
//public class ServerApplication {
//    private static final Logger logger = LogManager.getLogger(ServerApplication.class);
//    private final String fileName;
//    private final CollectionManager collectionManager;
//    private final CommandHandler commandHandler;
//    private final int port;
//    private ServerConnect serverConnect;
//    private ServerResponseSender responseSender;
//
//
//    public ServerApplication(String fileName, CollectionManager collectionManager, CommandHandler commandHandler, int port) {
//        this.fileName = fileName;
//        this.collectionManager = collectionManager;
//        this.commandHandler = commandHandler;
//        this.port = port;
//        init();
//    }
//
//    public ServerApplication(int port) {
//        this(
//                "collection.csv",
//                new CollectionManager(),
//                new CommandHandler(),
//                port
//        );
//    }
//
//    private void initializeCommands() {
//        commandHandler.register(new ExitCommand(collectionManager));
//        commandHandler.register(new AddCommand(collectionManager, null));
//        commandHandler.register(new InfoCommand(collectionManager));
//        commandHandler.register(new ShowCommand(collectionManager));
//        commandHandler.register(new UpdateCommand(collectionManager, null));
//        commandHandler.register(new ClearCommand(collectionManager));
//        commandHandler.register(new RemoveByIdCommand(collectionManager));
//        commandHandler.register(new AddIfMinCommand(collectionManager, null));
//        commandHandler.register(new RemoveGreaterCommand(collectionManager, null));
//        commandHandler.register(new RemoveLowerCommand(collectionManager, null));
//        commandHandler.register(new CountGreaterThanTypeCommand(collectionManager));
//        commandHandler.register(new FilterByRefundableCommand(collectionManager));
//        commandHandler.register(new FilterGreaterThanPersonCommand(collectionManager));
//        commandHandler.register(new HelpCommand(commandHandler));
//    }
//
//    private void init() {
//        try {
//            serverConnect = new ServerConnect(port);
//            responseSender = new ServerResponseSender(serverConnect.getSocket());
//            handleSaveOnTerminate();
//        } catch (IOException e) {
//            logger.error("Ошибка инициализации сервера: ", e);
//            System.exit(1);
//        }
//    }
//
//    private void handleSaveOnTerminate() {
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            logger.info("Сохранение коллекции при завершении...");
//            commandHandler.register(new SaveCommand(collectionManager));
//            if (serverConnect != null) {
//                serverConnect.closeSocket();
//            }
//        }));
//    }
//
//    public void start() {
//        logger.info("Сервер запущен на порту: {}", port);
//        while (true) {
//            try {
//                WorkThisMessage message = serverConnect.workThisMessage();
//                Object response = message.getMessage();
//                responseSender.sendResponse(
//                        response,
//                        message.getClientIp(),
//                        message.getClientPort()
//                );
//            } catch (Exception e) {
//                logger.error("Ошибка обработки запроса: ", e);
//            }
//        }
//    }
//}

