package com.example.RentACar.model.response;

public class GetUserResponseModel {
    private String username;
    private String email;
    private String first_name;
    private String last_name;
    private String phone_number;
    private String personal_number;
    private String image;

    public GetUserResponseModel(String username, String email, String first_name, String last_name,
                                String phone_number, String personal_number, String image) {
        this.username = username;
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_number = phone_number;
        this.personal_number = personal_number;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
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

    @Override
    public String toString() {
        return "GetUserResponseModel{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                '}';
    }
}
