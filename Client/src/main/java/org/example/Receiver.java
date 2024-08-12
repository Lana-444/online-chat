package org.example;

import java.io.*;

public class Receiver implements Runnable {

    private BufferedReader reader;

    public Receiver(BufferedReader reader){
        this.reader = reader;
    }
public void run() {
        try {
            String message;
            while ((message = reader.readLine()) != null){
                System.out.println(message);
                logMessage(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void logMessage(String message) {
        try {
            FileWriter fw = new FileWriter(Client.LOG_FILE, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);{
                out.println(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
