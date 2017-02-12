package pl.com.bottega.photostock.sales.network;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(9696);
        while (true) {
            Socket socket = serverSocket.accept();
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()).length() > 0) {
                System.out.println(line);
            }
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            pw.println("HTTP/1.1 200 OK \r\n");
            pw.println("Siema siema ziomek");
            pw.close();
        }
    }

}
