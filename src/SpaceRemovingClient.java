import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.channels.Selector;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class SpaceRemovingClient {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try {
            // Создание сокета и соединение с сервером
            SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT));
            clientChannel.configureBlocking(false);
            Selector selector = Selector.open();
            clientChannel.register(selector, SelectionKey.OP_READ);

            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите строки. Для выхода наберите 'end'.");

            while (true) {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("end")) {
                    break;
                }

                // Отправка строки на сервер
                ByteBuffer buffer = ByteBuffer.wrap(input.getBytes());
                clientChannel.write(buffer);

                // Чтение ответа от сервера
                selector.select();
                for (SelectionKey key : selector.selectedKeys()) {
                    if (key.isReadable()) {
                        ByteBuffer responseBuffer = ByteBuffer.allocate(256);
                        clientChannel.read(responseBuffer);
                        String response = new String(responseBuffer.array()).trim();
                        System.out.println("Обработанный ответ от сервера: " + response);
                    }
                }
                selector.selectedKeys().clear();
            }

            // Закрытие соединения
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
