package pl.com.bottega.photostock.sales.chat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    public static void main(String[] args) throws IOException {
        //Socket socket = new Socket("10.80.21.160", 9999);
        Socket socket = new Socket("localhost", 9999);
        InputStream is = socket.getInputStream();
        new Thread(new Receiver(is)).start();
        OutputStream os = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(os);
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String message = scanner.nextLine();
            pw.println(message);
            pw.flush();
        }
    }

    static class Receiver implements Runnable {

        private InputStream is;

        public Receiver(InputStream is) {
            this.is = is;
        }

        @Override
        public void run() {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            while (true) {
                try {
                    String message = bufferedReader.readLine();
                    if (message == null) {
                        System.out.println("Connection closed");
                        return;
                    }
                    System.out.println("Received: " + message);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

}
