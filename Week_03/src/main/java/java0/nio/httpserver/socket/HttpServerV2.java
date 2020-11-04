package java0.nio.httpserver.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServerV2 extends HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8802);
        System.out.println("serverSocket = " + serverSocket);
        
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(()-> service(socket)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
