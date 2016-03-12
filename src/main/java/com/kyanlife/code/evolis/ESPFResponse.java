package com.kyanlife.code.evolis;

/**
 * Created by kevinyan on 2/27/16.
 */
public class ESPFResponse {


    String id;
    static final String jsonrpc = "2.0";

    String result;

    public String toString() {
        return "Response OK";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public static ESPFResponse genericOKResponse(String id) {
        ESPFResponse response = new ESPFResponse();
        response.setId(id);
        response.setResult("OK");

        return response;
    }

    public static ESPFResponse genericErrorResponse(String id) {
        ESPFResponse response = new ESPFResponse();
        response.setId(id);
        response.setResult("Error");

        return response;
    }
}
