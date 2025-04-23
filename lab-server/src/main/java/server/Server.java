package server;





import controller.commands.Command;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.bind(new InetSocketAddress(port));
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            System.out.println("Server started on port " + port);

            while (true) {
                buffer.clear();
                SocketAddress clientAddress = channel.receive(buffer);
                buffer.flip();

                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
                Command command = (Command) ois.readObject();

                System.out.println("Received command: " + command.getName());

                String response = "Command " + command.getName() + " executed.";
                sendResponse(channel, clientAddress, response);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(DatagramChannel channel, SocketAddress clientAddress, String response) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(response);
        oos.flush();

        ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
        channel.send(buffer, clientAddress);
    }

    public static void main(String[] args) {
        Server server = new Server(9999);
        server.start();
    }
}
