package com.kyanlife.code.evolis;

/**
 * Created by kevinyan on 3/14/16.
 */
public class ESPFRequestParameters {

    String command;
    String device;
    Integer timeout;
    StringBuilder data;
    String session;
    String face;
    String panel;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public StringBuilder getData() {
        return data;
    }

    public void setData(StringBuilder data) {
        this.data = data;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getPanel() {
        return panel;
    }

    public void setPanel(String panel) {
        this.panel = panel;
    }

    @Override
    public String toString() {
        return "ESPFRequestParameters{" +
                "command='" + command + '\'' +
                ", device='" + device + '\'' +
                ", timeout=" + timeout +
                ", data='" + data + '\'' +
                ", session='" + session + '\'' +
                ", face='" + face + '\'' +
                ", panel='" + panel + '\'' +
                '}';
    }
}
