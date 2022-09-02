import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class MainClient {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        // запускаємо сокет на порту 56121
        Socket clientSocket = new Socket("127.0.0.1", 56120);

        System.out.println("--- Input x ---");
        int x = scanner.nextInt();

        // записуємо х в outputstream сокета
        clientSocket.getOutputStream().write(x);

        // вичитуємо з інпут стріма результат від f(x)
        boolean fxResponse = clientSocket.getInputStream().read() == 1;
        // вичитуємо з інпут стріма результат від g(x)
        boolean gxResponse =  clientSocket.getInputStream().read() == 1;
        System.out.println("");
        System.out.println("f(x) = " + fxResponse);
        System.out.println("g(x) = " + gxResponse);
        System.out.println();
        System.out.println("f(x) && g(x) = " + (fxResponse && gxResponse));

        // закриваємо сокет
        clientSocket.close();
    }


}
