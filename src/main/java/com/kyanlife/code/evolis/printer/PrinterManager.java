package com.kyanlife.code.evolis.printer;


import com.kyanlife.code.evolis.ESPFRequest;
import com.kyanlife.code.evolis.ESPFResponse;
import com.kyanlife.code.evolis.events.PrintEvent;
import com.kyanlife.code.evolis.listener.PrintEventListener;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kevinyan on 2/28/16.
 */
public class PrinterManager {

    Logger logger = LoggerFactory.getLogger(PrinterManager.class);


    HashMap<String, Printer> printers = new HashMap<String, Printer>();
    static PrinterManager printerManager = new PrinterManager();

    List<PrintEventListener> eventListeners = new ArrayList<PrintEventListener>();

    private PrinterManager () {

    }

    public static PrinterManager getInstance() {
        return printerManager;
    }

    public static void addPrintEventListener (PrintEventListener listener) {
        printerManager.eventListeners.add(listener);
    }

    public void addPrinter (String printerName, String ribbonType) {
        Printer printer = new Printer(printerName, ribbonType);
        printers.put(printerName, printer);
    }

    public Printer getPrinter (String printerName) {
        return printers.get(printerName);
    }

    public ESPFResponse processRequest (ESPFRequest request) {
        ESPFResponse response = ESPFResponse.genericErrorResponse(request.getId());

        logger.debug("Process request - method: " + request.getMethod());

        // Handle request command
        if ( "CMD.SendCommand".equalsIgnoreCase(request.getMethod()) ) {
            String command = request.getParams().get("command");

            //
            if ( "Ss".equalsIgnoreCase(command) ) {
                String deviceName = request.getParams().get("device");

                if ( printers.containsKey(deviceName) ) {
                    response = ESPFResponse.genericOKResponse(request.getId());
                } else {
                    response.setResult("Device not found");
                }
            }

            // handle print session start
        } else if ( "PRINT.Begin".equalsIgnoreCase(request.getMethod()) ) {
            String deviceName = request.getParams().get("device");

            if (printers.containsKey(deviceName)) {
                Printer printer = printerManager.getPrinter(deviceName);
                String jobId = printer.initialJob();
                response = ESPFResponse.genericOKResponse(request.getId());
                response.setResult(jobId);
            } else {
                response.setResult("Device not found");
            }
        } else if ( "PRINT.Set".equalsIgnoreCase(request.getMethod()) ) {
            String deviceName = request.getParams().get("device");

            if (printers.containsKey(deviceName)) {
                Printer printer = printerManager.getPrinter(deviceName);
                String jobId = printer.initialJob();
                response = ESPFResponse.genericOKResponse(request.getId());
                response.setResult(jobId);
            } else {
                response.setResult("Device not found");
            }
        } else if ( "PRINT.SetBitmap".equalsIgnoreCase(request.getMethod()) ) {
            try {
                String bitmapString = request.getParams().get("data");
                PrintEvent printEvent = new PrintEvent("SetBitmap", bitmapString);

                eventListeners.stream().forEach(l -> l.handlePrintEvent(printEvent));

                response = ESPFResponse.genericOKResponse(request.getId());

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if ( "PRINT.Print".equalsIgnoreCase(request.getMethod()) ) {
            String deviceName = request.getParams().get("device");

            if (printers.containsKey(deviceName)) {
                Printer printer = printerManager.getPrinter(deviceName);
                String jobId = printer.initialJob();
                response = ESPFResponse.genericOKResponse(request.getId());
                response.setResult(jobId);
            } else {
                response.setResult("Device not found");
            }
        } else if ( "PRINT.End".equalsIgnoreCase(request.getMethod()) ) {
            String deviceName = request.getParams().get("device");

            if (printers.containsKey(deviceName)) {
                Printer printer = printerManager.getPrinter(deviceName);
                String jobId = printer.initialJob();
                response = ESPFResponse.genericOKResponse(request.getId());
                response.setResult(jobId);
            } else {
                response.setResult("Device not found");
            }
        } else {
            response.setResult("Unknown method:" + request.getMethod());
        }

        logger.debug("Response : " + response);

        return response;
    }

}
