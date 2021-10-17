package com.example.RentACar.model.response;

public class AdminChangeUserInfoResponseModel {
    private boolean successful;
    private String info;

    public AdminChangeUserInfoResponseModel(boolean successful, String info) {
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
