package com.kyanlife.code.evolis.events;

/**
 * Created by kevinyan on 3/7/16.
 */
public class PrintEvent {

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

    @Override
    public String toString() {
        return "PrintEvent{" +
                "eventType='" + eventType + '\'' +
                ", eventDescription='" + eventDescription + '\'' +
                '}';
    }
}