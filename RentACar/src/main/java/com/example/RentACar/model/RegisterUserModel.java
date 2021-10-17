package com.example.RentACar.model;

import java.util.UUID;

public class RegisterUserModel {
    private UUID user_id;
    private String username;
    private String email;
    private String password;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String personal_number;
    private String image;
    private boolean admin;

    public RegisterUserModel(UUID user_id, String username, String email, String password, String first_name, String last_name,
                             String phone_number, String personal_number, String image, boolean admin) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.personal_number = personal_number;
        this.image = image;
        this.admin = false;
    }

    public RegisterUserModel(String username, String email, String password) {
        this.username=username;
        this.email=email;
        this.password=password;
    }

    public UUID getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getPersonal_number() {
        return personal_number;
    }

    public String getImage() {
        return image;
    }

    public boolean isAdmin() {
        return admin;
    }
}
