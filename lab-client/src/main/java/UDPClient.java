import ui.Request;
import ui.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class UDPClient {
    private static final int BUFFER_SIZE = 65536;
    private static final String HOST = "localhost";
    private static final int PORT = 12345;
    private static final int TIMEOUT = 3000;

    private DatagramChannel channel;
    private Selector selector;
    private InetSocketAddress serverAddress;

    public UDPClient() throws IOException {
        this.channel = DatagramChannel.open();
        this.channel.configureBlocking(false);
        this.selector = Selector.open();
        this.channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        this.serverAddress = new InetSocketAddress(HOST, PORT);
    }

    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Клиент запущен. Введите команду (или 'exit' для выхода):");

            while (true) {
                System.out.print("> ");
                if (!scanner.hasNextLine()) {
                    Thread.sleep(100);
                    continue;
                }

                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;
                if ("exit".equalsIgnoreCase(input)) break;

                // Отправка запроса
                String[] parts = input.split(" ", 2);
                String commandName = parts[0];
                String[] args = parts.length > 1 ? parts[1].split(" ") : new String[0];
                Request request = new Request(commandName, args);

                sendRequest(request);

                // Ожидание ответа
                Response response = receiveResponse();
                if (response != null) {
                    System.out.println(response.getMessage());
                } else {
                    System.out.println("Не получен ответ от сервера");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка клиента: " + e.getMessage());
        } finally {
            try {
                channel.close();
                selector.close();
            } catch (IOException e) {
                System.err.println("Ошибка при закрытии ресурсов: " + e.getMessage());
            }
        }
    }

    private void sendRequest(Request request) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(request);
        oos.flush();
        byte[] data = baos.toByteArray();

        ByteBuffer buffer = ByteBuffer.wrap(data);
        channel.send(buffer, serverAddress);
    }

    private Response receiveResponse() throws IOException {
        long startTime = System.currentTimeMillis();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);

        while (System.currentTimeMillis() - startTime < TIMEOUT) {
            selector.select(100);
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isReadable()) {
                    buffer.clear();
                    java.net.SocketAddress address = channel.receive(buffer);
                    if (address != null) {
                        buffer.flip();
                        String responseStr = StandardCharsets.UTF_8.decode(buffer).toString();
                        return new Response(true, responseStr);
                    }
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        UDPClient client = new UDPClient();
        client.start();
    }
}
