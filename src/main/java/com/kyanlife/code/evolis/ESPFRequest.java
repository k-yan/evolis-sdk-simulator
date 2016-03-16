package com.kyanlife.code.evolis;


import java.util.Map;

/**
 * Created by kevinyan on 2/27/16.
 */
public class ESPFRequest {


    String id;
    String jsonrpc = "2.0";
    String method;
    ESPFRequestParameters params;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ESPFRequestParameters getParams() {
        return params;
    }

    public void setParams(ESPFRequestParameters params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "ESPFRequest{" +
                "id='" + id + '\'' +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", method='" + method + '\'' +
                ", params=" + params.toString() +
                '}';
    }
}
