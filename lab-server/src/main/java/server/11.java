package server;

//public class ServerApplication11 {
//    private static final Logger logger = LogManager.getLogger(ServerApplication.class);
//    private final CollectionManager collectionManager;
//    private final CommandHandler commandHandler;
//    private final int port;
//    private ServerConnectionListener connectionListener;
//    private ServerResponseSender responseSender;
//
//    public ServerApplication(int port) {
//        this.port = port;
//        this.collectionManager = new CollectionManager();
//        this.commandHandler = new CommandHandler();
//        initializeCommands();
//        init();
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
//            connectionListener = new ServerConnectionListener(port);
//            responseSender = new ServerResponseSender(connectionListener.getSocket());
//            handleSaveOnTerminate();
//        } catch (IOException e) {
//            logger.error("Ошибка при инициализации сервера: ", e);
//            System.exit(1);
//        }
//    }
//
//    private void handleSaveOnTerminate() {
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            logger.info("Сервер завершает работу, сохраняем коллекцию...");
//            Command command = new SaveCommand(collectionManager);
//            command.execute(null);
//            connectionListener.closeSocket();
//        }));
//    }
//
//    public void start() {
//        logger.info("Сервер работает на порту: " + port);
//        while (true) {
//            try {
//                IncomingMessage incomingMessage = connectionListener.listenAndReceiveMessage();
//                Request request = incomingMessage.request();
//                logger.info("Получен запрос: " + request.getCommandName());
//
//                Command command = commandHandler.getCommand(request.getCommandName());
//                if (command == null) {
//                    responseSender.sendResponse(Response.failure("Неизвестная команда."), incomingMessage);
//                } else {
//                    Response response = command.execute(request);
//                    responseSender.sendResponse(response, incomingMessage);
//                }
//            } catch (Exception e) {
//                logger.error("Ошибка обработки запроса: ", e);
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        int port = 5000;
//        if (args.length > 0) {
//            try {
//                port = Integer.parseInt(args[0]);
//            } catch (NumberFormatException e) {
//                logger.error("Неверный формат порта, будет использоваться порт по умолчанию: 5000", e);
//            }
//        }
//        new ServerApplication(port).start();
//    }
//}
//
//class ServerConnectionListener {
//    private static final Logger logger = LogManager.getLogger(ServerConnectionListener.class);
//    private final DatagramChannel channel;
//    private final Selector selector;
//
//    public ServerConnectionListener(int port) throws IOException {
//        this.channel = DatagramChannel.open();
//        this.channel.configureBlocking(false);
//        this.channel.socket().bind(new InetSocketAddress(port));
//        this.selector = Selector.open();
//        this.channel.register(selector, SelectionKey.OP_READ);
//        logger.info("Сервер запущен на порту " + port);
//    }
//
//    public DatagramChannel getSocket() {
//        return this.channel;
//    }
//
//    public IncomingMessage listenAndReceiveMessage() throws IOException {
//        selector.select();
//        Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
//
//        while (keys.hasNext()) {
//            SelectionKey key = keys.next();
//            keys.remove();
//
//            if (key.isReadable()) {
//                DatagramChannel channel = (DatagramChannel) key.channel();
//                ByteBuffer buffer = ByteBuffer.allocate(8192);
//                SocketAddress clientAddress = channel.receive(buffer);
//
//                if (clientAddress != null) {
//                    buffer.flip();
//                    try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer.array()))) {
//                        Request request = (Request) ois.readObject();
//                        return new IncomingMessage(request, clientAddress);
//                    } catch (ClassNotFoundException e) {
//                        logger.error("Ошибка десериализации запроса: ", e);
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    public void closeSocket() {
//        try {
//            channel.close();
//        } catch (IOException e) {
//            logger.error("Ошибка при закрытии канала: ", e);
//        }
//    }
//}
//
////class ServerResponseSender {
////    private static final Logger logger = LogManager.getLogger(ServerResponseSender.class);
////    private final DatagramChannel channel;
////
////    public ServerResponseSender(DatagramChannel channel) {
////        this.channel = channel;
////    }
//
//    public void sendResponse(Response response, IncomingMessage incomingMessage) {
//        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
//             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
//
//            oos.writeObject(response);
//            oos.flush();
//
//            ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
//            channel.send(buffer, incomingMessage.clientAddress());
//            logger.info("Ответ отправлен: " + response.getMessage());
//
//        } catch (IOException e) {
//            logger.error("Ошибка отправки ответа: ", e);
//        }
//    }
//}
//
//record IncomingMessage(Request request, SocketAddress clientAddress) {
//}
