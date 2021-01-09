package com.hims.controller.request;


import com.fasterxml.jackson.annotation.JsonProperty;

public class AddPatientRequest {
    private String ENurseId;
    private String name;
    private String age;
    private String phone;
    private String address;
    private String NATResult;
    private String rating;
    private String testDate;
    private String testTime;

    public AddPatientRequest() {
    }

    public AddPatientRequest(String ENurseId, String name, String age, String phone,
                             String address, String NATResult, String rating, String testDate, String testTime) {
        this.ENurseId = ENurseId;
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.address = address;
        this.NATResult = NATResult;
        this.rating = rating;
        this.testDate = testDate;
        this.testTime = testTime;
    }

    public String getENurseId() {
        return ENurseId;
    }

    public void setENurseId(String ENurseId) {
        this.ENurseId = ENurseId;
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

    public String getNATResult() {
        return NATResult;
    }

    public void setNATResult(String NATResult) {
        this.NATResult = NATResult;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTestDate() {
        return testDate;
    }

    public void setTestDate(String testDate) {
        this.testDate = testDate;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }
}
