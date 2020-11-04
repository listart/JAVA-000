package java0.nio.httpserver.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServerV1 extends HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8801);
        System.out.println("serverSocket = " + serverSocket);

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                service(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
