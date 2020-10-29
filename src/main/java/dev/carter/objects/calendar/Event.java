package dev.carter.objects.calendar;

import java.sql.Date;

public class Event {

    private int eventId;
    private Date eventDate;
    private String eventDesc;

    public Event(int eventId, Date eventDate, String eventDesc) {
        this.eventDate = eventDate;
        this.eventDesc = eventDesc;
        this.eventId = eventId;
    }
    
    //Getters and setters
    public int getEventId() {
        return eventId;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }
    
}
