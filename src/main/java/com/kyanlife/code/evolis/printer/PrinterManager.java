package com.kyanlife.code.evolis.printer;


import com.kyanlife.code.evolis.ESPFRequest;
import com.kyanlife.code.evolis.ESPFResponse;
import com.kyanlife.code.evolis.events.CreatePrintJobEvent;
import com.kyanlife.code.evolis.events.PrintEvent;
import com.kyanlife.code.evolis.events.SetBitmapEvent;
import com.kyanlife.code.evolis.listener.PrintEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        ESPFResponse response = ESPFResponse.genericErrorResponse(request.getMethod());

        logger.debug("Process request - method: " + request.getMethod());

        broadcastPrintEvent(new PrintEvent("Print request method:" + request.getMethod()));

        switch (request.getMethod().toLowerCase()) {
            // Handle request command
            case "cmd.sendcommand": {
                String command = request.getParams().getCommand();

                // handle server status request
                if ( "Ss".equalsIgnoreCase(command) ) {
                    String deviceName = request.getParams().getDevice();

                    if ( printers.containsKey(deviceName) ) {
                        response = ESPFResponse.genericOKResponse(request.getId());
                    } else {
                        response.setResult("Device not found");
                    }
                } else {
                    response.setResult("Command not found");
                }

            }
                // handle print initiate request
            case "print.begin" : {

                Printer printer = getPrinter(request);

                if ( printer != null ) {
                    String jobId = printer.createJob();
                    response = ESPFResponse.genericOKResponse(request.getId());
                    response.setResult(jobId);

                    broadcastPrintEvent(new CreatePrintJobEvent(jobId));
                } else {
                    response.setResult("Device not found");
                }

            }

            case "print.set": {
                String deviceName = request.getParams().getDevice();

                if (printers.containsKey(deviceName)) {
                    Printer printer = printerManager.getPrinter(deviceName);
                    String jobId = printer.createJob();
                    response = ESPFResponse.genericOKResponse(request.getId());
                    response.setResult(jobId);
                } else {
                    response.setResult("Device not found");
                }
            }

            case "print.setbitmap": {
                try {
                    String bitmapString = request.getParams().getData();

                    broadcastPrintEvent(new SetBitmapEvent(request.getParams().getFace(),
                            bitmapString));

                    response = ESPFResponse.genericOKResponse(request.getId());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            case "print.print": {
                Printer printer = getPrinter(request);

                if (printer != null) {
                    String jobId = printer.createJob();
                    response = ESPFResponse.genericOKResponse(request.getId());
                    response.setResult(jobId);
                } else {
                    response.setResult("Device not found");
                }

            }

            case "print.end": {
                String deviceName = request.getParams().getDevice();

                if (printers.containsKey(deviceName)) {
                    Printer printer = printerManager.getPrinter(deviceName);
                    String jobId = printer.createJob();
                    response = ESPFResponse.genericOKResponse(request.getId());
                    response.setResult(jobId);
                } else {
                    response.setResult("Device not found");
                }
            }

        }

        logger.debug("Response : " + response.toString());

        return response;
    }

    private Printer getPrinter (ESPFRequest request) {

        Printer printer = null;
        String deviceName = request.getParams().getDevice();

        if (printers.containsKey(deviceName)) {
            printer = printerManager.getPrinter(deviceName);
        }
        return printer;
    }

    private PrintJob getPrintJob (ESPFRequest request) {
        return null;
    }

    private void broadcastPrintEvent (PrintEvent printEvent) {
        eventListeners.stream().forEach(l -> l.handlePrintEvent(printEvent));
    }
}
