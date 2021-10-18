package com.example.RentACar.model.response.user;

public class RegisterUserResponseModel {
    private boolean successful;
    private String message;

    public RegisterUserResponseModel(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getMessage() {
        return message;
    }
}
