package com.example.RentACar.model.response.contract;

public class SignedContractResponseModel {
    private boolean successful;
    private String info;

    public SignedContractResponseModel(boolean successful, String info) {
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
