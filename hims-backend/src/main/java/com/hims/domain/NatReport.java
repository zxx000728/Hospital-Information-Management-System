package com.hims.domain;

import java.sql.Date;
import java.sql.Time;

public class NatReport {
    private int id;
    private int p_id;
    private String result;
    private Date date;
    private Time time;
    private String rating;

    public NatReport() {
    }

    public NatReport(int id, int p_id, String result, Date date, Time time, String rating) {
        this.id = id;
        this.p_id = p_id;
        this.result = result;
        this.date = date;
        this.time = time;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
