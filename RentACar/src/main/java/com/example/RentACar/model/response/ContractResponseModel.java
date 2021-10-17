package com.example.RentACar.model.response;

import java.time.LocalDate;

public class ContractResponseModel {
    private String contract_id;
    private String user_id;
    private String car_id;
    private LocalDate start_date;
    private LocalDate end_date;
    private double totalPrice;
    private boolean signed;
    private boolean approved;
    public ContractResponseModel(String contractId, String userId, String carId,
                                 LocalDate startDate,
                                 LocalDate endDate, double totalPrice,
                                 boolean signed, boolean approved) {
        this.contract_id = contractId;
        this.user_id = userId;
        this.car_id = carId;
        this.start_date = startDate;
        this.end_date = endDate;
        this.totalPrice = totalPrice;
        this.signed = signed;
        this.approved = approved;
    }
    public String getContractId() {
        return contract_id;
    }
    public String getUserId() {
        return user_id;
    }
    public String getCarId() {
        return car_id;
    }
    public LocalDate getStartDate() {
        return start_date;
    }
    public LocalDate getEndDate() {
        return end_date;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
    public boolean isSigned() {
        return signed;
    }
    public boolean isApproved() {
        return approved;
    }
}
