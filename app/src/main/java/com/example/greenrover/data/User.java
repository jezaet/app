package com.example.greenrover.data;

public class User {

    public String FirstName;
    public String LastName;
    public String Address;
    public String Postcode;
    public String PhoneNum;
    public String Email;


    public User(String firstName, String lastName, String address, String postcode, String phoneNum, String email){
        this.FirstName = firstName;
        this.LastName = lastName;
        this.Address = address;
        this.Postcode = postcode;
        this.PhoneNum = phoneNum;
        this.Email = email;
    }

}

