package com.hims.domain;

public class DailyReport {
    private int id;
    private int p_id;
    private String date;
    private String temperature;
    private String symptom;
    private String state;
    private int w_nurse_id;
//    private int n_report_id;

    public DailyReport() {
    }

    public DailyReport(int id, int p_id, String date, String temperature, String symptom, String state, int w_nurse_id) {
        this.id = id;
        this.p_id = p_id;
        this.date = date;
        this.temperature = temperature;
        this.symptom = symptom;
        this.state = state;
        this.w_nurse_id = w_nurse_id;
    }

//    public DailyReport(int id, int p_id, String date, String temperature, String symptom, String state, int w_nurse_id, int n_report_id) {
//        this.id = id;
//        this.p_id = p_id;
//        this.date = date;
//        this.temperature = temperature;
//        this.symptom = symptom;
//        this.state = state;
//        this.w_nurse_id = w_nurse_id;
//        this.n_report_id = n_report_id;
//    }

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getW_nurse_id() {
        return w_nurse_id;
    }

    public void setW_nurse_id(int w_nurse_id) {
        this.w_nurse_id = w_nurse_id;
    }

//    public int getN_report_id() {
//        return n_report_id;
//    }
//
//    public void setN_report_id(int n_report_id) {
//        this.n_report_id = n_report_id;
//    }
}
