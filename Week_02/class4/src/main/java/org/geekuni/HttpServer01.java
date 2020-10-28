package org.geekuni;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer01 {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8801);
        System.out.println("serverSocket = " + serverSocket);

        while (true) {
            try {
                Socket socket = serverSocket.accept();
                service(socket);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private static void service(Socket socket) {
        try {
            Thread.sleep(20);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String s;
            while ((s=in.readLine()) != null) {
                if ("".equals(s))
                        break;
            }

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            printWriter.println();
            printWriter.write("hello.nio");

            printWriter.close();
            in.close();
            socket.close();

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
