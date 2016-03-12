package com.kyanlife.code.evolis.io;

import com.kyanlife.code.evolis.ESPFRequest;
import com.kyanlife.code.evolis.ESPFRequestListener;
import com.kyanlife.code.evolis.ESPFResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.fasterxml.jackson.databind.*;
import com.kyanlife.code.evolis.printer.PrinterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kevinyan on 2/27/16.
 */
public class JSONRequestHandler extends Thread {

    Logger logger = LoggerFactory.getLogger(JSONRequestHandler.class);

    private Socket socket = null;
    ESPFRequestListener listener;

    public JSONRequestHandler(Socket socket) {
        super("Socket request handler");
        this.socket = socket;
       // this.listener = listener;
    }

    public void run() {

        try {

            // read request


            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            String inputLine;
            StringBuilder requestString = new StringBuilder();

            boolean hasMoreInput = true;
            while ( hasMoreInput && !socket.isInputShutdown() ) {
                inputLine = in.readLine();
                if ( inputLine == null ) {
                    hasMoreInput = false;
                } else {
                    requestString.append(inputLine);
                }
            }

            // send response
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.append(handleRequest(requestString.toString()));
            out.close();

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String handleRequest (String requestString) {
        String responseString;

        try {

            logger.debug("Request string: " + requestString);

            ObjectMapper jsonMapper = new ObjectMapper();

            ESPFRequest requestObj = jsonMapper.readValue(requestString, ESPFRequest.class);

            logger.debug("Request object: " + requestObj);

            ESPFResponse responseObj = PrinterManager.getInstance().processRequest(requestObj);

            responseString = jsonMapper.writeValueAsString(responseObj);

        } catch (Exception e) {
            logger.error("Error when handling request", e);
            responseString = "{ error } ";
        }
        return responseString;
    }
}
