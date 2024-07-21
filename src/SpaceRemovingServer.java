import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.net.InetSocketAddress;

public class SpaceRemovingServer {
    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        try {
            // Создаем селектор и серверный канал
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress("localhost", PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Сервер запущен на порту " + PORT);

            while (true) {
                // Ожидание событий
                selector.select();

                // Обработка событий
                for (SelectionKey key : selector.selectedKeys()) {
                    if (key.isAcceptable()) {
                        SocketChannel clientChannel = serverSocketChannel.accept();
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                        System.out.println("Клиент подключился: " + clientChannel.getRemoteAddress());
                    }
                    if (key.isReadable()) {
                        handleClient(key, selector);
                    }
                }

                selector.selectedKeys().clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(SelectionKey key, Selector selector) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(256);
        int bytesRead = clientChannel.read(buffer);

        if (bytesRead == -1) {
            clientChannel.close();
            return;
        }

        String received = new String(buffer.array()).trim();
        String processed = received.replaceAll("\\s+", ""); // Удаляем все пробельные символы

        // Отправляем результат обратно клиенту
        buffer.clear();
        buffer.put(processed.getBytes());
        buffer.flip();
        clientChannel.write(buffer);
    }
}
