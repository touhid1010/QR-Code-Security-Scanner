package com.netizenbd.netichecker.sqlitedatabase;

import java.util.Date;

/**
 * Created by Md. Touhidul Islam on 11/20/2016.
 */

public class DataEntity {

    private String tableColumnId;
    private String eventID;
    private String eventName;
    private String participateID;
    private String name;
    private String phone;
    private String area;
    private Date dateTime;

    public DataEntity() {

    }

    public DataEntity(String eventID, String eventName, String participateID, String name, String phone, String area) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.participateID = participateID;
        this.name = name;
        this.phone = phone;
        this.area = area;
    }

    public DataEntity(String tableColumnId, String eventID, String eventName, String participateID, String name, String phone, String area, Date dateTime) {
        this.tableColumnId = tableColumnId;
        this.eventID = eventID;
        this.eventName = eventName;
        this.participateID = participateID;
        this.name = name;
        this.phone = phone;
        this.area = area;
        this.dateTime = dateTime;
    }

    public String getTableColumnId() {
        return tableColumnId;
    }

    public void setTableColumnId(String tableColumnId) {
        this.tableColumnId = tableColumnId;
    }

    public String getEventID() {
        return eventID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getParticipateID() {
        return participateID;
    }

    public void setParticipateID(String participateID) {
        this.participateID = participateID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
}
