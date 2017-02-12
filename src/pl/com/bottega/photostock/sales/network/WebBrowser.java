package pl.com.bottega.photostock.sales.network;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class WebBrowser {

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String address = scanner.nextLine();
        Socket socket = new Socket(address, 80);
        OutputStream os = socket.getOutputStream();
        os.write("GET / HTTP/1.1\n".getBytes());
        os.write(("Host: " + address + "\n").getBytes());
        os.write("\r\n".getBytes());

        InputStream is = socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        String line;
        for (int i = 0; i <= 5; i++) {
            line = bufferedReader.readLine();
            System.out.println(line);
        }

        /*
        while ((line = bufferedReader.readLine()) != null) {
            System.out.println(line);
        }
        */
    }

}
