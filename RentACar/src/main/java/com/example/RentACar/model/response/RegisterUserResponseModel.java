package com.example.RentACar.model.response;

public class RegisterUserResponseModel {
    private boolean successfull;
    private String message;
    public RegisterUserResponseModel(boolean successfull, String message) {
        this.successfull = successfull;
        this.message = message;
    }
    public boolean isSuccessfull() {
        return successfull;
    }
    public String getMessage() {
        return message;
    }
}
