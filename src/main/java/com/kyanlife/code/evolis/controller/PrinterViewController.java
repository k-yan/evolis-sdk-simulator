package com.kyanlife.code.evolis.controller;

import com.kyanlife.code.evolis.MainProperties;
import com.kyanlife.code.evolis.printer.Printer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by kevinyan on 3/19/16.
 */
public class PrinterViewController implements Initializable {

    Logger logger = LoggerFactory.getLogger(PrinterViewController.class);

    @FXML
    private TextField hostName;

    @FXML
    private TextField printerName;

    @FXML
    private TextField serverPort;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.hostName.setText(
                    InetAddress.getLocalHost().getHostName() + ":"
                            + InetAddress.getLocalHost().getHostAddress());

            this.printerName.setText(
                    MainProperties.get("app.printer.name"));

            this.serverPort.setText(
                    MainProperties.get("app.server.port"));
        } catch (Exception e) {
            logger.error("Error initializing  PrinterViewController", e);
        }
    }

}
