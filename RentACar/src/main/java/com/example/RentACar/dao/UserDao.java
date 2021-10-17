package com.example.RentACar.dao;

import com.example.RentACar.connection.DatabaseConnection;
import com.example.RentACar.model.AdminUpdateUserModel;
import com.example.RentACar.model.RegisterUserModel;
import com.example.RentACar.model.UpdateUserModel;
import com.example.RentACar.model.response.GetUserResponseModel;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public interface UserDao {
    Connection conn = DatabaseConnection.getConnection();
    boolean userNameExists (String username);
    boolean emailExists (String email);
    String getPasswordWithIdentification (String identification);
    String getPasswordWithId (UUID id);
    GetUserResponseModel getUser (String id);
    boolean isAdmin (String id);
    List<GetUserResponseModel> getAllUsers();
    void registerUser (RegisterUserModel user);
    void updateUser (UpdateUserModel user, UUID id);
    void adminUpdateUserInfo (AdminUpdateUserModel user, String id);
}
