package com.example.RentACar.model;

import java.util.UUID;

public class UsersModel {
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
    public UsersModel(UUID user_id, String username, String email, String password, String first_name, String last_name,
                      String phone_number, String personal_number, String image, boolean admin) {
        this.user_id = user_id = UUID.randomUUID();
        this.username = username;
        this.email = email;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.personal_number = personal_number;
        this.image = image;
        this.admin = admin;
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
}
