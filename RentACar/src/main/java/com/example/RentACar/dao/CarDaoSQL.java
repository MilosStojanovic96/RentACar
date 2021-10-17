package com.example.RentACar.dao;

import com.example.RentACar.model.CarsModel;
import com.example.RentACar.model.SearchCarModel;
import com.example.RentACar.model.request.ChangeCarInfoRequestModel;
import com.example.RentACar.model.response.GetCarResponseModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarDaoSQL implements CarDao{

    private ContractDao contractDao = new ContractDaoSQL();
    private static PreparedStatement preparedStatement;
    private static Statement statement;
    @Override
    public List<GetCarResponseModel> getAllCars() {
        List<GetCarResponseModel> allCars = new ArrayList<>();
        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM cars");
            while (rs.next()) {
                GetCarResponseModel car = new GetCarResponseModel(rs.getString(1),
                        rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getInt(5), rs.getInt(6), rs.getString(7),
                        rs.getDouble(8), rs.getInt(9), rs.getString(10),
                        rs.getInt(11), rs.getBoolean(12), rs.getString(13),
                        rs.getString(14));
                allCars.add(car);
            }
            return allCars;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allCars;
    }
    @Override
    public List<GetCarResponseModel> searchCars(SearchCarModel searchCarModel, List<GetCarResponseModel> cars) {
        List<GetCarResponseModel> searchResult = new ArrayList<>();
        if (searchCarModel.getAutomatic() == null && searchCarModel.getDoors() == null
                && searchCarModel.getModel() == null && searchCarModel.getMake() == null &&searchCarModel.getPower() == null
                && searchCarModel.getPrice() == null && searchCarModel.getYear() == null){
            return cars;
        }
        for (var car : cars){
            if ((searchCarModel.getAutomatic() == null || searchCarModel.getAutomatic() == car.isAutomatic())
                    && (searchCarModel.getYear() == null || searchCarModel.getYear() <= car.getYear())
                    && (searchCarModel.getDoors() == null || searchCarModel.getDoors() == car.getDoors())
                    && (searchCarModel.getModel() == null || car.getModel().toLowerCase().contains(searchCarModel.getModel().toLowerCase()))
                    && (searchCarModel.getMake() == null || car.getMake().toLowerCase().contains(searchCarModel.getMake().toLowerCase()))
                    && (searchCarModel.getPower() == null || searchCarModel.getPower() >= car.getPower())
                    && (searchCarModel.getPrice() == null || searchCarModel.getPrice() >= car.getPrice())){
                searchResult.add(car);
            }
        }
        return searchResult;
    }
    @Override
    public List<GetCarResponseModel> getAvailableCars(LocalDate startDate, LocalDate endDate) {
        List<GetCarResponseModel> allCars = getAllCars();
        List<GetCarResponseModel> availableCars = new ArrayList<>();
        for (var car : allCars){
            if (isCarAvailable(startDate, endDate, car.getCar_id())){
                availableCars.add(car);
            }
        }
        return availableCars;
    }
    @Override
    public boolean isCarAvailable(LocalDate startDate, LocalDate endDate, String carId) {
        List<LocalDate> desiredDates = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());
        List<LocalDate> unavailableDates = contractDao.getCarOccupiedDates(carId);
        for (var date : desiredDates){
            if (unavailableDates.contains(date)){
                return false;
            }
        }
        return true;
    }
    @Override
    public GetCarResponseModel getCar(String id) {
        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM cars WHERE car_id = '" + id + "'");
            rs.next();
            return new GetCarResponseModel(rs.getString(1),
                    rs.getString(2), rs.getString(3), rs.getString(4),
                    rs.getInt(5), rs.getInt(6), rs.getString(7),
                    rs.getDouble(8), rs.getInt(9), rs.getString(10),
                    rs.getInt(11), rs.getBoolean(12), rs.getString(13),
                    rs.getString(14));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
    @Override
    public void updateCarInfo(String id, ChangeCarInfoRequestModel carInfo) {
        try {
            preparedStatement = conn.prepareStatement("UPDATE cars " +
                    " SET licence_plate = ?," +
                    " make = ?," +
                    " model = ?," +
                    " year = ?," +
                    " engine_capacity = ?," +
                    " color = ?," +
                    " price = ?," +
                    " doors = ?," +
                    " size = ?," +
                    " power = ?," +
                    " automatic = ?," +
                    " fuel = ?," +
                    " image = ? WHERE car_id = '" + id + "';");
            //statement = conn.createStatement();
            //ResultSet rs = statement.executeQuery()
            preparedStatement.setString(1, carInfo.getLicence_plate());
            preparedStatement.setString(2, carInfo.getMake());
            preparedStatement.setString(3, carInfo.getModel());
            preparedStatement.setInt(4, carInfo.getYear());
            preparedStatement.setInt(5, carInfo.getEngine_capacity());
            preparedStatement.setString(6, carInfo.getColor());
            preparedStatement.setDouble(7, carInfo.getPrice());
            preparedStatement.setInt(8, carInfo.getDoors());
            preparedStatement.setString(9, carInfo.getSize());
            preparedStatement.setInt(10, carInfo.getPower());
            preparedStatement.setBoolean(11, carInfo.isAutomatic());
            preparedStatement.setString(12, carInfo.getFuel());
            preparedStatement.setString(13, carInfo.getImage());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void delete(String id) {
        try {
            preparedStatement = conn.prepareStatement("DELETE FROM cars WHERE car_id = ?");
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void addCar(CarsModel carInfo) {
        try {
            preparedStatement = conn.prepareStatement("INSERT INTO cars " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, carInfo.getCar_id().toString());
            preparedStatement.setString(2, carInfo.getLicence_plate());
            preparedStatement.setString(3, carInfo.getMake());
            preparedStatement.setString(4, carInfo.getModel());
            preparedStatement.setInt(5, carInfo.getYear());
            preparedStatement.setInt(6, carInfo.getEngine_capacity());
            preparedStatement.setString(7, carInfo.getColor());
            preparedStatement.setDouble(8, carInfo.getPrice());
            preparedStatement.setInt(9, carInfo.getDoors());
            preparedStatement.setString(10, carInfo.getSize());
            preparedStatement.setInt(11, carInfo.getPower());
            preparedStatement.setBoolean(12, carInfo.isAutomatic());
            preparedStatement.setString(13, carInfo.getFuel());
            preparedStatement.setString(14, carInfo.getImage());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public double getPrice(String id) {
        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT price FROM cars " +
                    "WHERE car_id = '" + id + "';");
            rs.next();
            return rs.getDouble(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
