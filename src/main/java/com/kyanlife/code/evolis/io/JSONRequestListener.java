package com.kyanlife.code.evolis.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by kevinyan on 2/28/16.
 */
public class JSONRequestListener extends Thread {

    Logger logger = LoggerFactory.getLogger(JSONRequestListener.class);

    Integer listeningPort;
    ServerSocket serverSocket;
    Boolean listening = true;

    public JSONRequestListener(Integer listeningPort) {
        this.listeningPort = listeningPort;
        try {
            serverSocket = new ServerSocket(listeningPort);
        } catch (Exception e) {
            logger.error("Cannot listen to socket port", e);
        }
    }

    public void run () {
        try {
            while (listening) {
                logger.debug("Waiting for request");
                new JSONRequestHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            logger.error("Could not listen on port ");
        }
    }

    public void stopListening () {
        try {
            listening = false;
            serverSocket.close();
        } catch (Exception e) {
            logger.error("Error while stopping listener", e);
        }
    }
}
