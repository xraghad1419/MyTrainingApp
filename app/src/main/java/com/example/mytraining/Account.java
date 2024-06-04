package com.example.mytraining;

public class Account {
    private String Name ;
    private String Email ;
    private String Password ;
    private String userType;

    public Account() {
    }

    public Account(String name, String email, String password, String userType) {
        Name= name;
        Email = email;
        Password = password;
        this.userType = userType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}

