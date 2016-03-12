package com.kyanlife.code.evolis.listener;

import com.kyanlife.code.evolis.events.PrintEvent;

/**
 * Created by kevinyan on 3/7/16.
 */
public interface PrintEventListener {

    void handlePrintEvent (PrintEvent e);
}
