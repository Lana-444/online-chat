package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientHandler implements Runnable {
    private Socket socket;
    private String username;
    private PrintWriter writer;
    private BufferedReader reader;


    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
            this.username = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            String message;
            while ((message = reader.readLine()) != null) {
                if (message.equalsIgnoreCase("выход")) {
                    break;
                }

                String timestampedMessage = formatMessage(message);
                System.out.println(timestampedMessage);
                ChatServer.broadcastMessage(timestampedMessage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }


    private String formatMessage(String message) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String timeStamp = sdf.format(new Date());
        return timeStamp + " : " + username + " : " + message;
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    private void cleanup() {
        IOException exception = null;

        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            exception = e;
            e.printStackTrace();
        } finally {
            reader = null;
        }

        try {
            if (writer != null) {
                writer.close();
            }
        } finally {
            writer = null;
        }

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            if (exception == null) {
                exception = e;
            }
            e.printStackTrace();
        } finally {
            socket = null;
        }


        ChatServer.clientHandlers.remove(this);


        if (exception != null) {
            throw new RuntimeException("Во время очистки произошла ошибка", exception);
        }
    }

}





