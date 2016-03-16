package com.kyanlife.code.evolis.controller;

import com.kyanlife.code.evolis.events.CreatePrintJobEvent;
import com.kyanlife.code.evolis.events.PrintEvent;
import com.kyanlife.code.evolis.events.SetBitmapEvent;
import com.kyanlife.code.evolis.listener.PrintEventListener;
import com.kyanlife.code.evolis.printer.PrinterManager;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by kevinyan on 3/12/16.
 */
public class IDCardViewController implements PrintEventListener {

    private static String DEFAULT_IMAGE_PATH = "com/kyanlife/code/evolis/fxml/BlankImage.bmp";

    @FXML private Label printJobId;

    @FXML private ImageView cardFrontImageView;

    @FXML private Image cardFrontImage;

    @FXML private ImageView cardBackImageView;

    @FXML private Image cardBackImage;

    @FXML private TextArea requestLogArea;

    public IDCardViewController () {
        PrinterManager.addPrintEventListener(this);
    }

    public void handlePrintEvent (PrintEvent printEvent) {
        try {

            if ( printEvent instanceof CreatePrintJobEvent ) {
                CreatePrintJobEvent createPrintJobEvent = (CreatePrintJobEvent) printEvent;
                printJobId = new Label(createPrintJobEvent.getJobId());

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

                    byte[] imageByte = Base64.decodeBase64(imageString);

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

        }
    }

    private void setBackImage (InputStream is) {
        try {
            cardBackImage = new Image(is);
            cardBackImageView.setImage(cardBackImage);
        } catch (Exception e) {

        }
    }
}


