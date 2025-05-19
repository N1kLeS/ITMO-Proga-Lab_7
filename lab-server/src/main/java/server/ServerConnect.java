package server;

import ui.Serialization;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerConnect {
    private static final Logger logger = LogManager.getLogger(ServerConnect.class.getName());
    private final int port;
    private final DatagramSocket socket;

    public ServerConnect(int port) throws SocketException {
        this.port = port;
        this.socket = new DatagramSocket(port);
        logger.info("Сервер запустится на порту " + port);
    }

    public WorkThisMessage workThisMessage() throws IOException, ClassNotFoundException {
        byte[] buffer = new byte[65536];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        logger.info("Сервер получил пакет");
        socket.receive(packet);

        Object data = Serialization.deserialize(packet.getData());
        logger.info("Получено " + data);

        return new WorkThisMessage(data, packet.getAddress(), packet.getPort());
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void closeSocket() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }
}
