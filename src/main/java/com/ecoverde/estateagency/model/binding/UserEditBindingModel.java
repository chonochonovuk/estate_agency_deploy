package com.ecoverde.estateagency.model.binding;

import com.ecoverde.estateagency.model.validator.FieldMatch;

import javax.validation.constraints.NotEmpty;

@FieldMatch(first = "passwordNew",
        second = "confirmNewPassword",
        message = "The passwords do not match")
public class UserEditBindingModel {

    private String username;
    private String password;
    private String passwordNew;
    private String confirmNewPassword;

    public UserEditBindingModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @NotEmpty(message = "Enter password!")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotEmpty(message = "Enter password!")
    public String getPasswordNew() {
        return passwordNew;
    }

    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    @NotEmpty(message = "Enter password!")
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
