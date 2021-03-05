package com.ecoverde.estateagency.model.binding;

import com.ecoverde.estateagency.model.validator.FieldMatch;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.Pattern;

@FieldMatch(first = "password",
        second = "confirmPassword",
        message = "The passwords do not match")
public class UserRegisterBindingModel {

    private String username;
    private String password;
    private String confirmPassword;
    private String phoneNumber;
    private String firstName;
    private String lastName;

    public UserRegisterBindingModel() {
    }

    @Length(min = 4, message = "Minimum 4 characters length")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Length(min = 5, message = "Minimum 5 characters length")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


    @Pattern(regexp = "(\\+359|0)[0-9]{9}",
    message = "Enter valid phone number!")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Length(min = 4, message = "Minimum 4 characters length")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Length(min = 4, message = "Minimum 4 characters length")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
