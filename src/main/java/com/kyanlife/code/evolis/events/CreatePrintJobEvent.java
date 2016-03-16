package com.kyanlife.code.evolis.events;

/**
 * Created by kevinyan on 3/14/16.
 */
public class CreatePrintJobEvent extends PrintEvent {

    String jobId;

    public CreatePrintJobEvent (String jobId) {
        super("Create new print job");
        this.jobId = jobId;
    }

    public String getJobId () {
        return jobId;
    }
}
