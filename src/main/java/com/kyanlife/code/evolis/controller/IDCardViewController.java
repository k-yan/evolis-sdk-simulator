package com.kyanlife.code.evolis.controller;

import com.kyanlife.code.evolis.events.CreatePrintJobEvent;
import com.kyanlife.code.evolis.events.PrintEvent;
import com.kyanlife.code.evolis.events.SetBitmapEvent;
import com.kyanlife.code.evolis.listener.PrintEventListener;
import com.kyanlife.code.evolis.printer.PrinterManager;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by kevinyan on 3/12/16.
 */
public class IDCardViewController implements Initializable, PrintEventListener {

    Logger logger = LoggerFactory.getLogger(IDCardViewController.class);

    private static String DEFAULT_IMAGE_PATH = "/com/kyanlife/code/evolis/fxml/BlankImage.bmp";

    @FXML
    private TextField printJobId;

    @FXML
    private TextField requestHost;

    @FXML private ImageView cardFrontImageView;

    @FXML private Image cardFrontImage;

    @FXML private ImageView cardBackImageView;

    @FXML private Image cardBackImage;

    @FXML private TextArea requestLogArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PrinterManager.addPrintEventListener(this);
    }

    public void handlePrintEvent (PrintEvent printEvent) {
        try {

            if ( printEvent instanceof CreatePrintJobEvent ) {
                CreatePrintJobEvent createPrintJobEvent = (CreatePrintJobEvent) printEvent;

                printJobId.setText(createPrintJobEvent.getJobId());
                requestHost.setText(createPrintJobEvent.getRequestHost());

                setBackImage(IDCardViewController.class.getResourceAsStream(DEFAULT_IMAGE_PATH));
                setFrontImage(IDCardViewController.class.getResourceAsStream(DEFAULT_IMAGE_PATH));

            } else if ( printEvent instanceof SetBitmapEvent ) {
                SetBitmapEvent bitmapEvent = (SetBitmapEvent) printEvent;

                String imageString = bitmapEvent.getBitmapString();

                if ( imageString != null ) {
                    // remove encoding prefix
                    if (imageString.startsWith("base64:")) {
                        imageString = imageString.substring(7);
                    }

                    byte[] imageByte = Base64.decodeBase64(imageString.getBytes("UTF-8"));

                    if ("back".equals(bitmapEvent.getFace())) {
                        setBackImage(new ByteArrayInputStream(imageByte));
                    } else {
                        setFrontImage(new ByteArrayInputStream(imageByte));
                    }
                }
            } else {
                requestLogArea.appendText(printEvent.getEventDescription());
                requestLogArea.appendText("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFrontImage (InputStream is) {
        try {
            cardFrontImage = new Image(is);
            cardFrontImageView.setImage(cardFrontImage);
        } catch (Exception e) {
            logger.error("Error while setting front image: " + e);
        }
    }

    private void setBackImage (InputStream is) {
        try {
            cardBackImage = new Image(is);
            cardBackImageView.setImage(cardBackImage);
        } catch (Exception e) {
            logger.error("Error while setting back image: " + e);
        }
    }
}


