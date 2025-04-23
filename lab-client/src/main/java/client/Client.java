package client;

import common.SaveCommand;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Client {
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() {
        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            SaveCommand command = new SaveCommand();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(command);
            oos.flush();

            buffer.put(baos.toByteArray());
            buffer.flip();
            channel.send(buffer, new InetSocketAddress(host, port));

            System.out.println("Command sent: " + command.getName());

            receiveResponse(channel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveResponse(DatagramChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketAddress address = channel.receive(buffer);

        if (address != null) {
            buffer.flip();
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(buffer.array()));
            try {
                String response = (String) ois.readObject();
                System.out.println("Response from server: " + response);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No response received.");
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 9999);
        client.start();
    }
}
