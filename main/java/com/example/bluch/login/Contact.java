package com.example.bluch.login;

public class Contact {
    private int id;
    private String name;
    private String PhoneNumber;

    public Contact(String name, String phoneNumber) {
        this.name = name;
        PhoneNumber = phoneNumber;
    }

    public Contact(int id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        PhoneNumber = phoneNumber;
    }

    public Contact() {
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

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
