package com.example.RentACar.dao;

import com.example.RentACar.connection.DatabaseConnection;
import com.example.RentACar.model.SearchCarModel;
import com.example.RentACar.model.request.car.AddCarRequestModel;
import com.example.RentACar.model.request.car.ChangeCarInfoRequestModel;
import com.example.RentACar.model.response.car.GetCarResponseModel;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public interface CarDao {
    Connection conn = DatabaseConnection.getConnection();
    List<GetCarResponseModel> getAllCars();
    List<GetCarResponseModel> searchCars(SearchCarModel searchCarModel, List<GetCarResponseModel> cars);
    List<GetCarResponseModel> getAvailableCars(LocalDate startDate, LocalDate endDate);
    boolean isCarAvailable(LocalDate startDate, LocalDate endDate, String carId);
    GetCarResponseModel getCar(String id);
    void updateCarInfo(String id, ChangeCarInfoRequestModel carInfo);
    void delete(String id);
    void addCar(AddCarRequestModel car);
    double getPrice(String id);
}
