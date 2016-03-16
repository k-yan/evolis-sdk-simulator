package com.kyanlife.code.evolis.printer;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by kevinyan on 2/27/16.
 */
public class PrintJob {

    Logger logger = LoggerFactory.getLogger(PrintJob.class);

    static String PRINT_JOB_CREATED = "Created";
    static String PRINT_JOB_ENDED = "complete";
    static String PRINT_JOB_ID_FORMAT = "JOB%06d";

    String jobId;
    String printSettings;
    String status;

    byte[] frontBitmap;
    byte[] backBitmap;

    public PrintJob (int jobId) {
        this.jobId = String.format(PRINT_JOB_ID_FORMAT, jobId);
        this.status = PRINT_JOB_CREATED;
        logger.debug("Started job:" + this.jobId);
    }

    public String getJobId() {
        return jobId;
    }

    public String getStatus() {
        return status;
    }

    public String getPrintSettings() {
        return printSettings;
    }

    public void finishJob () {
        this.status = PRINT_JOB_ENDED;
    }

    public byte[] getFrontBitmap() {
        return frontBitmap;
    }

    public void setFrontBitmap(byte[] frontBitmap) {
        this.frontBitmap = frontBitmap;
    }

    public byte[] getBackBitmap() {
        return backBitmap;
    }

    public void setBackBitmap(byte[] backBitmap) {
        this.backBitmap = backBitmap;
    }

}
