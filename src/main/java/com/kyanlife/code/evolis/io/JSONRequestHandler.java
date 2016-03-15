package com.kyanlife.code.evolis.io;

import com.kyanlife.code.evolis.ESPFRequest;
import com.kyanlife.code.evolis.ESPFRequestListener;
import com.kyanlife.code.evolis.ESPFResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Calendar;

import com.fasterxml.jackson.databind.*;
import com.kyanlife.code.evolis.printer.PrinterManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kevinyan on 2/27/16.
 */
public class JSONRequestHandler extends Thread {

    Logger logger = LoggerFactory.getLogger(JSONRequestHandler.class);

    // maximum time to read request input
    static int REQUEST_INPUT_TIMEOUT = 5000;

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
            long requestStatTime = Calendar.getInstance().getTimeInMillis();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));
            String inputLine;
            StringBuilder requestString = new StringBuilder();

            boolean hasMoreInput = true;
            while ( hasMoreInput ) {
                inputLine = in.readLine();
                if ( inputLine == null ) {
                    hasMoreInput = false;
                } else {
                    requestString.append(inputLine);
                }

                // if request string is valid JSON, then request is complete
                if ( isJSONValid (requestString.toString()) ) {
                    hasMoreInput = false;
                }

                // if request input timeout reached, stop reading input.
                if ( Calendar.getInstance().getTimeInMillis() - requestStatTime > REQUEST_INPUT_TIMEOUT ) {
                    hasMoreInput = false;
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

    public boolean isJSONValid (String jsonInString) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
