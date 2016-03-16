package com.kyanlife.code.evolis.events;

import com.kyanlife.code.evolis.ESPFRequestParameters;

/**
 * Created by kevinyan on 3/14/16.
 */
public class SetBitmapEvent extends PrintEvent {

    String face;
    String bitmapString;


    public SetBitmapEvent (String face, String bitmapString) {
        super("Setting card bitmap");
        this.face = face;
        this.bitmapString = bitmapString;
    }

    public String getFace() {
        return face;
    }

    public String getBitmapString() {
        return bitmapString;
    }

}
