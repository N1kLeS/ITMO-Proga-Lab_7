package server;

import controller.CommandHandler;
import controller.ElementInputHandler;
import controller.commands.*;
import service.CollectionManager;
import ui.Request;
import ui.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

public class UDPServer {
    private static final int BUFFER_SIZE = 65536;
    private static final int PORT = 12345;

    private final CommandHandler commandHandler;
    private final CollectionManager collectionManager;
    private DatagramChannel channel;
    private Selector selector;

    public UDPServer() {
        this.collectionManager = new CollectionManager();
        this.commandHandler = new CommandHandler();
        initializeCommands();
    }

    private void initializeCommands() {
        ElementInputHandler inputHandler = new ElementInputHandler();

        commandHandler.register(new ExitCommand(collectionManager));
        commandHandler.register(new AddCommand(collectionManager, inputHandler));
        commandHandler.register(new InfoCommand(collectionManager));
        commandHandler.register(new ShowCommand(collectionManager));
        commandHandler.register(new UpdateCommand(collectionManager, inputHandler));
        commandHandler.register(new ClearCommand(collectionManager));
        commandHandler.register(new RemoveByIdCommand(collectionManager));
        commandHandler.register(new AddIfMinCommand(collectionManager, inputHandler));
        commandHandler.register(new RemoveGreaterCommand(collectionManager, inputHandler));
        commandHandler.register(new RemoveLowerCommand(collectionManager, inputHandler));
        commandHandler.register(new CountGreaterThanTypeCommand(collectionManager));
        commandHandler.register(new FilterByRefundableCommand(collectionManager));
        commandHandler.register(new FilterGreaterThanPersonCommand(collectionManager));
        commandHandler.register(new HelpCommand(commandHandler));
    }

    public void start() {
        try {
            selector = Selector.open();
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            channel.socket().bind(new java.net.InetSocketAddress(PORT));
            channel.register(selector, SelectionKey.OP_READ);

            System.out.println("UDP сервер запущен на порту " + PORT);

            while (true) {
                selector.select();
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        DatagramChannel channel = (DatagramChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        java.net.SocketAddress clientAddress = channel.receive(buffer);

        if (clientAddress != null) {
            buffer.flip();
            try {
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);

                // Десериализация запроса
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                ObjectInputStream ois = new ObjectInputStream(bais);
                Request request = (Request) ois.readObject();

                // Обработка команды
                Response response = commandHandler.executeCommand(request.getCommandName(), request);

                // Отправка ответа
                byte[] responseData = response.toString().getBytes();
                ByteBuffer responseBuffer = ByteBuffer.wrap(responseData);
                channel.send(responseBuffer, clientAddress);
            } catch (ClassNotFoundException e) {
                System.err.println("Ошибка десериализации: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        UDPServer server = new UDPServer();
        server.collectionManager.loadCollection("collection.csv");
        server.start();
    }
}


