import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class MainServer {

    private static Socket clientSocket;

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        // відкриваємо сервер сокет на порті 56121
        ServerSocket serverSocket = new ServerSocket(56120);

        // очікуємо першого звернення від клієнт сокета
        clientSocket = serverSocket.accept();

        // вичитуємо х з інпут стріма сокета
        int x = clientSocket.getInputStream().read();

        System.out.println("Received x:" + x);
        System.out.println("----------");

        // створюємо екзекютор сервіс з фіксованим пулом средів = 2 штуки
        ExecutorService taskExecutor = Executors.newFixedThreadPool(2);

        // комплітбл фючер, щоб виконати f(x) = x % 2
        CompletableFuture<Integer> fx = CompletableFuture.supplyAsync(() -> {
            System.out.println("F(x) thread started::" + Thread.currentThread().getName());
            return x % 2;
        }, taskExecutor);

        // комплітбл фючер, щоб виконати g(x) = x % 5
        CompletableFuture<Integer> gx = CompletableFuture.supplyAsync(() -> {
            System.out.println("G(x) thread started::" + Thread.currentThread().getName());
            return x % 5;
        }, taskExecutor);

        // закриваємо екзекютор сервіс
        taskExecutor.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("f(x) = " + (fx.get() == 0));
        System.out.println("g(x) = " + (gx.get() == 0));

        // записуємо результат f(x) в атпут стрім сокета
        clientSocket.getOutputStream().write(fx.get() % 2 == 0 ? 1 : 0);
        // записуємо результат g(x) в атпут стрім сокета
        clientSocket.getOutputStream().write(gx.get() % 5 == 0 ? 1 : 0);

        // закриваємо сокети
        serverSocket.close();
        clientSocket.close();
    }

}
