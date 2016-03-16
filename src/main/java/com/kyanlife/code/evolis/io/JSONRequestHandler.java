package com.kyanlife.code.evolis.io;

import com.kyanlife.code.evolis.ESPFRequest;
import com.kyanlife.code.evolis.ESPFRequestListener;
import com.kyanlife.code.evolis.ESPFResponse;

import java.io.*;
import java.net.Socket;
import java.util.Calendar;

import com.fasterxml.jackson.databind.*;
import com.kyanlife.code.evolis.MainProperties;
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

            logger.debug("Starting server socket");

            // read request
            long requestStatTime = Calendar.getInstance().getTimeInMillis();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            socket.getInputStream()));

            logger.debug("Reading socket input stream: " + in);
            String inputLine;
            StringBuilder requestString = new StringBuilder();

            boolean hasMoreInput = true;
            while ( hasMoreInput ) {

                inputLine = in.readLine();

                if ( inputLine == null ) {
                    logger.debug("Input stream is NULL");
                    hasMoreInput = false;
                } else {
                    requestString.append(inputLine);

                    // if request string is valid JSON, then request is complete
                    if ( inputLine.trim().endsWith("}")
                            && isJSONValid (requestString.toString()) ) {
                        logger.debug("Request is valid JSON object");
                        hasMoreInput = false;
                    }
                }



                // if request input timeout reached, stop reading input.
                if ( hasMoreInput
                        && Calendar.getInstance().getTimeInMillis() - requestStatTime > REQUEST_INPUT_TIMEOUT ) {
                    logger.debug("Request timeout");
                    hasMoreInput = false;
                }

                Thread.sleep(100);
            }

            // add response delay to simulate network or resource delay
            Thread.sleep(MainProperties.getInteger("app.response.delay"));

            logger.debug("Try to handle request: " + requestString);

            // send response
            OutputStream out = socket.getOutputStream();
            out.write(handleRequest(requestString.toString()).getBytes("UTF-8"));
            out.close();

            socket.close();
        } catch (Exception e) {
            logger.error("Error while processing request: " + e);
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
            logger.debug("Input string is valid JSON");
            return true;
        } catch (IOException e) {
            logger.debug("Input string is NOT valid JSON");
            return false;
        }
    }
}
