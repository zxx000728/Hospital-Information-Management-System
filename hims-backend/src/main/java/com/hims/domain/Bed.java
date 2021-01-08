package com.hims.domain;

public class Bed {
    private int id;
    private int w_id;
    private String state;
    private int p_id;
    private String p_name;

    public Bed() {

    }

    public Bed(int id, int w_id, String state, int p_id, String p_name) {
        this.id = id;
        this.w_id = w_id;
        this.state = state;
        this.p_id = p_id;
        this.p_name = p_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getW_id() {
        return w_id;
    }

    public void setW_id(int w_id) {
        this.w_id = w_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }
}
