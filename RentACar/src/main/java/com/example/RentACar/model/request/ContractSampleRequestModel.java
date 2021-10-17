package com.example.RentACar.model.request;

import java.time.LocalDate;

public class ContractSampleRequestModel {
    private String user_id;
    private String car_id;
    private LocalDate start_date;
    private LocalDate end_date;

    public ContractSampleRequestModel(String user_id, String car_id, LocalDate start_date, LocalDate end_date) {
        this.user_id = user_id;
        this.car_id = car_id;
        this.start_date = start_date;
        this.end_date = end_date;
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
}
