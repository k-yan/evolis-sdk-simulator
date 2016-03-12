package com.kyanlife.code.evolis.controller;

import com.kyanlife.code.evolis.events.PrintEvent;
import com.kyanlife.code.evolis.listener.PrintEventListener;
import com.kyanlife.code.evolis.printer.PrinterManager;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;

/**
 * Created by kevinyan on 3/12/16.
 */
public class IDCardViewController implements PrintEventListener {

    @FXML
    private ImageView cardFrontImageView;

    @FXML private Image cardFrontImage;

    public IDCardViewController () {
        PrinterManager.addPrintEventListener(this);
    }

    public void handlePrintEvent (PrintEvent printEvent) {
        try {
            String imageString = printEvent.getEventDescription();

            // remove encoding prefix
            if ( imageString.startsWith("base64:") ) {
                imageString = imageString.substring(7);
            }

            byte[] imageByte = Base64.decodeBase64(imageString);

            cardFrontImage = new Image(new ByteArrayInputStream(imageByte));

            cardFrontImageView.setImage(cardFrontImage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }}
