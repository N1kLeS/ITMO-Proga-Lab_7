package server;
import controller.CommandHandler;
import controller.commands.*;
import service.CollectionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

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
        commandHandler.register(new ExitCommand(collectionManager));
        commandHandler.register(new AddCommand(collectionManager, null));
        commandHandler.register(new InfoCommand(collectionManager));
        commandHandler.register(new ShowCommand(collectionManager));
        commandHandler.register(new UpdateCommand(collectionManager, null));
        commandHandler.register(new ClearCommand(collectionManager));
        commandHandler.register(new RemoveByIdCommand(collectionManager));
        commandHandler.register(new AddIfMinCommand(collectionManager, null));
        commandHandler.register(new RemoveGreaterCommand(collectionManager, null));
        commandHandler.register(new RemoveLowerCommand(collectionManager, null));
        commandHandler.register(new CountGreaterThanTypeCommand(collectionManager));
        commandHandler.register(new FilterByRefundableCommand(collectionManager));
        commandHandler.register(new FilterGreaterThanPersonCommand(collectionManager));
        commandHandler.register(new HelpCommand(commandHandler));
    }

    public void start() {
        logger.info("Starting server on port {}", port);

        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.bind(new InetSocketAddress(port));
            channel.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

            while (isRunning) {
                processRequests(channel, buffer);
                Thread.sleep(50); // Для снижения нагрузки на CPU
            }
        } catch (IOException | InterruptedException e) {
            logger.error("Server error: {}", e.getMessage());
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
        logger.info("Server shutdown initiated");
    }
}
