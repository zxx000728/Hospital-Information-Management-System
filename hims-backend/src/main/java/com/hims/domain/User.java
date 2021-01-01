package com.hims.domain;

public class User {
    private int id;
    private String name;
    private String password;
    private String age;
    private String email;
    private String phone;
    private String u_type;

    public User() {
    }

    public User(int id, String name, String password, String age, String email, String phone, String u_type) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.age = age;
        this.email = email;
        this.phone = phone;
        this.u_type = u_type;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getU_type() {
        return u_type;
    }

    public void setU_type(String u_type) {
        this.u_type = u_type;
    }
}
