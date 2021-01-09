package com.hims.domain;

public class NatReport {
    private int id;
    private int p_id;
    private String result;
    private String date;
    private String time;
//    private String rating;

    public NatReport() {
    }

//    public NatReport(int p_id, String result, String date, String time, String rating) {
//        this.p_id = p_id;
//        this.result = result;
//        this.date = date;
//        this.time = time;
//        this.rating = rating;
//    }

    public NatReport(int p_id, String result, String date, String time) {
        this.p_id = p_id;
        this.result = result;
        this.date = date;
        this.time = time;
    }

//    public NatReport(int id, int p_id, String result, String date, String time, String rating) {
//        this.id = id;
//        this.p_id = p_id;
//        this.result = result;
//        this.date = date;
//        this.time = time;
//        this.rating = rating;
//    }

    public NatReport(int id, int p_id, String result, String date, String time) {
        this.id = id;
        this.p_id = p_id;
        this.result = result;
        this.date = date;
        this.time = time;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

//    public String getRating() {
//        return rating;
//    }
//
//    public void setRating(String rating) {
//        this.rating = rating;
//    }
}
