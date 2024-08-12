package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Properties;
import java.util.Scanner;

public class Client {
    private static final String SETTINGS_FILE = "settings.txt";
    static final String LOG_FILE = "file.log";
    private static String serverAddress;
    private static int serverPort;

    public static void main(String[] args) {
        loadSettings();
        try (Socket socket = new Socket(serverAddress, serverPort);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner =new Scanner(System.in)) {
            System.out.println("Введите имя: ");
            String name = scanner.nextLine();
            writer.println(name);

            Thread receiverThread = new Thread(new Receiver(reader));
            receiverThread.start();

            String message;
            while (true){
                message = scanner.nextLine();
                if(message.equalsIgnoreCase("/exit")){
                    break;
                }
                writer.println(message);
                logMessage (name + " : " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadSettings() {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream(SETTINGS_FILE)) {
            properties.load(input);
            serverAddress = properties.getProperty("server_address");
            serverPort = Integer.parseInt(properties.getProperty("port"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void logMessage(String message) {
        try (FileWriter fileWriter = new FileWriter(LOG_FILE,true);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter out = new PrintWriter(bufferedWriter)){
            out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
