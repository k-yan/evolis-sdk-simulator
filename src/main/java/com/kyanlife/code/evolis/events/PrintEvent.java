package com.kyanlife.code.evolis.events;

/**
 * Created by kevinyan on 3/7/16.
 */
public class PrintEvent {

    String requestHost;
    String eventType;
    String eventDescription;

    public PrintEvent() {

    }

    public PrintEvent(String eventDescription) {
        this.eventType = "Genric";
        this.eventDescription = eventDescription;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getRequestHost() {
        return requestHost;
    }

    public void setRequestHost(String requestHost) {
        this.requestHost = requestHost;
    }

    @Override
    public String toString() {
        return "PrintEvent{" +
                "requestHost='" + requestHost + '\'' +
                "eventType='" + eventType + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                '}';
    }
}