package com.example.RentACar.model.response.user;

import java.util.UUID;

public class LoginUserResponseModel {
    private boolean successful;
    private String info;

    public LoginUserResponseModel(boolean successful, String info) {
        this.successful = successful;
        this.info = info;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getInfo() {
        return info;
    }
}
