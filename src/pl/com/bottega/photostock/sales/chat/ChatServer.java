package pl.com.bottega.photostock.sales.chat;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ChatServer {

    private List<ClientConnection> connections = new LinkedList<>();

    public void loop() throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected");
            ClientConnection clientConnection = new ClientConnection(clientSocket);
            connections.add(clientConnection);
            new Thread(clientConnection).start();

        }
    }

    public static void main(String[] args) throws IOException {
        new ChatServer().loop();
    }

    public void sendToOthers(ClientConnection source, String message) {
        for (ClientConnection connection : connections) {
            if (source != connection) {
                connection.sendMessage(message);
            }
        }
    }

    public void removeConnection(ClientConnection connection) {
        connections.remove(connection);
    }

    class ClientConnection implements Runnable {

        private Socket clientSocket;

        public ClientConnection(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                InputStream is = clientSocket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                while (true) {
                    String line = bufferedReader.readLine();
                    if (line == null) {
                        removeConnection(this);
                        return;
                    }
                    System.out.println("Received message: " + line);
                    sendToOthers(this, line);
                }
            } catch (IOException e) {
                e.printStackTrace();
                removeConnection(this);
                return;
            }
        }

        public void sendMessage(String message) {
            try {
                OutputStream os = clientSocket.getOutputStream();
                PrintWriter printWriter = new PrintWriter(os);
                printWriter.println(message);
                printWriter.flush();
            } catch (IOException e) {
                removeConnection(this);
                e.printStackTrace();
            }
        }
    }

}