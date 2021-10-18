package com.example.RentACar.model.request.user;

public class LoginUserRequestModel {
    private String identification;
    private String password;

    public LoginUserRequestModel(String identification, String password) {
        this.identification = identification;
        this.password = password;
    }

    public String getIdentification() {
        return identification;
    }

    public String getPassword() {
        return password;
    }
}
