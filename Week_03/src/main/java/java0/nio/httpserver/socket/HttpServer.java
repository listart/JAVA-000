package java0.nio.httpserver.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HttpServer {
    static void service(Socket socket) {
        try {
            Thread.sleep(20);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                if ("".equals(line))
                    break;
            }

            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-Type:text/html;charset=utf-8");
            String body = "hello,nio";
            printWriter.println("Content-Length:"+ body.getBytes().length);
            printWriter.println();
            printWriter.write(body);
            printWriter.flush();
            printWriter.close();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }
}
