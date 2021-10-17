package com.example.RentACar.model.request;

public class ContractApprovalRequestModel {
    private boolean approved;
    public ContractApprovalRequestModel(boolean approved) {
        this.approved = approved;
    }
    public boolean isApproved() {
        return approved;
    }
}
