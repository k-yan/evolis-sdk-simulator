package com.kyanlife.code.evolis.printer;

import com.sun.javafx.binding.StringFormatter;

/**
 * Created by kevinyan on 2/27/16.
 */
public class PrintJob {

    String jobId;
    String printSettings;
    String status;

    public PrintJob (int jobId) {
        this.jobId = String.format("JOB%06d", jobId);
        System.out.println("Started job:" + this.jobId);
        this.status = "started";
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
        this.status = "finished";
    }
}
