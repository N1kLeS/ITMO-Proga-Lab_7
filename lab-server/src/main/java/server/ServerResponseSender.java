package server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.Serialization;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class ServerResponseSender {
    private static final Logger logger = LogManager.getLogger(ServerResponseSender.class);
    private final DatagramSocket socket;

    public ServerResponseSender(DatagramSocket socket) {
        this.socket = socket;

    }

    public void sendResponse(Object response, InetAddress clientAddress, int clientPort) throws IOException {
        byte[] responseData = Serialization.serialize(response);
        DatagramPacket responsePacket = new DatagramPacket(
                responseData,
                responseData.length,
                clientAddress,
                clientPort
        );
        socket.send(responsePacket);
    }
}
