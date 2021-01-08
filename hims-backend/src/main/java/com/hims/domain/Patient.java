package com.hims.domain;

public class Patient {
    private int id;
    private String name;
    private String age;
    private String phone;
    private String address;
    private String rating;
    private int e_nurse_id;
    private int w_nurse_id;
    private int t_area_id;
    private int w_id;
    private int bed_id;
    private String state;
    private boolean is_to_be_released;
    private boolean is_to_be_transferred;

    public Patient() {
    }

    public Patient(String name, String age, String phone, String address,
                   String rating, int e_nurse_id) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.rating = rating;
        this.e_nurse_id = e_nurse_id;
    }

    public Patient(int id, String name, String age, String phone, String address,
                   String rating, int e_nurse_id, int w_nurse_id, int t_area_id, int w_id, int bed_id, String state,
                   boolean is_to_be_released, boolean is_to_be_transferred) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.rating = rating;
        this.e_nurse_id = e_nurse_id;
        this.w_nurse_id = w_nurse_id;
        this.t_area_id = t_area_id;
        this.w_id = w_id;
        this.bed_id = bed_id;
        this.state = state;
        this.is_to_be_released = is_to_be_released;
        this.is_to_be_transferred = is_to_be_transferred;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getE_nurse_id() {
        return e_nurse_id;
    }

    public void setE_nurse_id(int e_nurse_id) {
        this.e_nurse_id = e_nurse_id;
    }

    public int getW_nurse_id() {
        return w_nurse_id;
    }

    public void setW_nurse_id(int w_nurse_id) {
        this.w_nurse_id = w_nurse_id;
    }

    public int getBed_id() {
        return bed_id;
    }

    public void setBed_id(int bed_id) {
        this.bed_id = bed_id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isIs_to_be_released() {
        return is_to_be_released;
    }

    public void setIs_to_be_released(boolean is_to_be_released) {
        this.is_to_be_released = is_to_be_released;
    }

    public boolean isIs_to_be_transferred() {
        return is_to_be_transferred;
    }

    public void setIs_to_be_transferred(boolean is_to_be_transferred) {
        this.is_to_be_transferred = is_to_be_transferred;
    }

    public int getT_area_id() {
        return t_area_id;
    }

    public void setT_area_id(int t_area_id) {
        this.t_area_id = t_area_id;
    }

    public int getW_id() {
        return w_id;
    }

    public void setW_id(int w_id) {
        this.w_id = w_id;
    }
}
