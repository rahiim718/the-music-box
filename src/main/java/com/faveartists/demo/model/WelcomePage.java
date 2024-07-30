package com.faveartists.demo.model;

import jakarta.persistence.*;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.Id;

@Entity
public class WelcomePage {
    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String userPassword;


    public WelcomePage(){}


    public WelcomePage(String firstName, String lastName, String emailAddress, String userPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.userPassword = userPassword;
    }

    public Long getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public String getEmailAddress() {
        return emailAddress;
    }

    public String getUserPassword() {
        return userPassword;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "WelcomePage [firstName=" + firstName + ", lastName=" + lastName + ", emailAddress=" + emailAddress
                + ", userPassword=" + userPassword + "]";
    }



}
