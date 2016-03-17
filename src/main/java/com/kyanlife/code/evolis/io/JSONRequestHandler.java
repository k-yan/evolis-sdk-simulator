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

    static int BUFFER_SIZE = 2048;

    private Socket socket = null;
    ESPFRequestListener listener;

    public JSONRequestHandler(Socket socket) {
        super("Socket request handler");
        this.socket = socket;
        try {
            this.socket.setSoTimeout(MainProperties.getInteger("app.socket.read.timeout"));
        } catch (Exception e) {
            logger.error("Error setting timeout on socket");
        }
       // this.listener = listener;
    }

    public void run() {

        StringBuilder requestString = new StringBuilder();

        try {

            logger.debug("Starting server socket");

            // read request
            long requestStatTime = Calendar.getInstance().getTimeInMillis();

            InputStreamReader in = new InputStreamReader(socket.getInputStream(), "UTF-8");

            logger.debug("Reading socket input stream: " + in);
            String inputLine;


            boolean hasMoreInput = true;

            char[] tempInputBuffer = new char[BUFFER_SIZE];

            while (hasMoreInput) {

                int amntRead = in.read(tempInputBuffer);

                logger.debug("Amount data read : " + amntRead);

                inputLine = new String(tempInputBuffer, 0, amntRead);

                requestString.append(inputLine);

                // if request string is valid JSON, then request is complete
                if (inputLine.trim().endsWith("}")
                        && isJSONValid(requestString.toString())) {
                    logger.debug("Request is valid JSON object");
                    hasMoreInput = false;
                }

            }

        } catch (Exception e) {
            logger.error("Error while reading input stream", e);
        }

        try {
            // add response delay to simulate network or resource delay
            //Thread.sleep(MainProperties.getInteger("app.response.delay"));

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
