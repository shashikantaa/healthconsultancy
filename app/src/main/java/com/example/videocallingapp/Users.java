package com.example.videocallingapp;

public class Users {
    public String uid;
    public String name;
    public String phone;
    public String email;
    public String image;
    public Users(){
        //empty constructor needed
    }

    public Users(String uid, String name, String phone, String email, String image) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
