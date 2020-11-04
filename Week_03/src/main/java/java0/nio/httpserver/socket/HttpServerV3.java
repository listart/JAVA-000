package java0.nio.httpserver.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServerV3 extends HttpServer{
    public static void main(String[] args) throws IOException {
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        ServerSocket serverSocket = new ServerSocket(8803);
        System.out.println("serverSocket = " + serverSocket);

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                threadPool.execute(()-> service(socket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
