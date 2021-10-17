package com.example.RentACar.dao;

import com.example.RentACar.connection.DatabaseConnection;
import com.example.RentACar.model.request.SignedContractRequestModel;
import com.example.RentACar.model.response.ContractResponseModel;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public interface ContractDao {
    Connection conn = DatabaseConnection.getConnection();
    List<LocalDate> getCarOccupiedDates(String car_id);
    List<ContractResponseModel> getAllContracts();
    List<ContractResponseModel> getAllPendingContracts();
    List<ContractResponseModel> getContractHistory (String user_id);
    void deleteContract (String contract_id);
    void updateContractApproval (String contract_id, boolean approval);
    boolean userHasPendingContract (String user_id);
    void addContractToDatabase(SignedContractRequestModel contract);
}
