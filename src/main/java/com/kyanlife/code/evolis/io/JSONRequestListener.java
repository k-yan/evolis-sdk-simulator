package com.kyanlife.code.evolis.io;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by kevinyan on 2/28/16.
 */
public class JSONRequestListener extends Thread {

    Integer listeningPort;
    ServerSocket serverSocket;
    Boolean listening = true;

    public JSONRequestListener(Integer listeningPort) {
        this.listeningPort = listeningPort;
        try {
            serverSocket = new ServerSocket(listeningPort);
        } catch (Exception e) {
            // unable to start server port
        }
    }

    public void run () {
        try {
            while (listening) {
                System.out.println("Listening for request: ");
                new JSONRequestHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port ");
        }
    }

    public void stopListening () {
        try {
            listening = false;
            serverSocket.close();
        } catch (Exception e) {

        }
    }
}
