package com.example.epokerproject.model;

public class User {

    public String userName;
    public String userPassword;
    public String userMobileno;
    public String userEmailid;
    public String userLicenceNumber;

    public User(){

    }

    public User(String userName, String userPassword, String userMobileno, String userEmailid, String userLicenceNumber){
        this.userName = userName;
        this.userPassword= userPassword;
        this.userMobileno= userMobileno;
        this.userEmailid = userEmailid;
        this.userLicenceNumber = userLicenceNumber;
    }
}
