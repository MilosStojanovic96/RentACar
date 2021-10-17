package com.example.RentACar.model.response;

import java.time.LocalDate;

public class ContractSampleResponseModel {
    private String user_id;
    private String car_id;
    private LocalDate start_date;
    private LocalDate end_date;
    private double total_price;
    private boolean signed;

    public ContractSampleResponseModel(String user_id, String car_id, LocalDate start_date, LocalDate end_date,
                                       double total_price, boolean signed) {
        this.user_id = user_id;
        this.car_id = car_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.total_price = total_price;
        this.signed = signed;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getCar_id() {
        return car_id;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public double getTotal_price() {
        return total_price;
    }

    public boolean isSigned() {
        return signed;
    }
}
