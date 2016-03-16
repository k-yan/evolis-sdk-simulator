package com.kyanlife.code.evolis.printer;

import java.util.HashMap;

/**
 * Created by kevinyan on 2/28/16.
 */
public class Printer {

    String deviceName;
    String ribbonType;
    int jobCounter = 1;

    HashMap<String, PrintJob> printJobs = new HashMap<String, PrintJob>();

    public Printer (String deviceName, String ribbonType) {
        this.deviceName = deviceName;
        this.ribbonType = ribbonType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getRibbonType() {
        return ribbonType;
    }

    public String createJob() {
        PrintJob printJob = new PrintJob(jobCounter);
        printJobs.put(printJob.getJobId(), printJob);
        jobCounter++;
        return printJob.getJobId();
    }
}
