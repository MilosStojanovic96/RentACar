package com.example.RentACar.controller;

import com.example.RentACar.dao.UserDao;
import com.example.RentACar.dao.UserDaoSQL;
import com.example.RentACar.model.RegisterUserModel;
import com.example.RentACar.model.UpdateUserModel;
import com.example.RentACar.model.request.user.ChangeUserInfoRequestModel;
import com.example.RentACar.model.request.user.LoginUserRequestModel;
import com.example.RentACar.model.request.user.RegisterUserRequestModel;
import com.example.RentACar.model.response.user.ChangeUserInfoResponseModel;
import com.example.RentACar.model.response.user.GetUserResponseModel;
import com.example.RentACar.model.response.user.LoginUserResponseModel;
import com.example.RentACar.model.response.user.RegisterUserResponseModel;
import org.springframework.web.bind.annotation.*;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
    public class UserController {
    private static UserDao userDao = new UserDaoSQL();

    private boolean isEmailValid(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasNumber = false;
        boolean hasLetter = false;
        char[] array = password.toCharArray();
        for (char c : array) {
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
            if (Character.isDigit(c)) {
                hasNumber = true;
            }
            if (hasLetter && hasNumber) {
                return true;
            }
        }
        return false;
    }

    private String getSHA256(String password) {
        String hashValue = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes());
            byte[] digestedBytes = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digestedBytes){
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @PostMapping("/users/register")
    public RegisterUserResponseModel register(@RequestBody RegisterUserRequestModel user) {
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();
        if (username.length() < 3) {
            return new RegisterUserResponseModel(false,
                    "Username has less than 3 characters!!");
        }
        if (!isPasswordValid(password)) {
            return new RegisterUserResponseModel(false,
                    "Password is invalid!!");
        }
        else {
            password = getSHA256(password);
        }
        if (!isEmailValid(email)) {
            return new RegisterUserResponseModel(false,
                    "Email is invalid!!");
        }
        if (userDao.userNameExists(username)) {
            return new RegisterUserResponseModel(false,
                    "Username already exists!!");
        }
        if (userDao.emailExists(email)) {
            return new RegisterUserResponseModel(false,
                    "Email already exists!!");
        }
        userDao.registerUser(new RegisterUserModel(username, email, password));
        return new RegisterUserResponseModel(true, "Successful registration");

    }

    @PostMapping("/users/login")
    public LoginUserResponseModel login(@RequestBody LoginUserRequestModel user) {
        String identification = user.getIdentification();
        String password = getSHA256(user.getPassword());
        if (userDao.emailExists(identification) || userDao.userNameExists(identification)) {
            if (password.equals(userDao.getPasswordWithIdentification(identification)))
                return new LoginUserResponseModel(true, password);
        }
        return new LoginUserResponseModel(false,
                "Wrong username/email or password");
    }

    // TODO proveriti phonenumber
    @PatchMapping("/users/{id}")
    public ChangeUserInfoResponseModel changeUserInfo(@PathVariable("id") String id,
                                                      @RequestBody ChangeUserInfoRequestModel user){
        String password = getSHA256(user.getPassword());
        String newPassword = user.getNewPassword();
        if (!userDao.getPasswordWithUUID(id).equals(password)){
            return new ChangeUserInfoResponseModel(false, "Wrong password!!");
        }
        if (!newPassword.isEmpty()) {
            if (!isPasswordValid(newPassword)) {
                return new ChangeUserInfoResponseModel(false, "New password is invalid!!");
            }
            newPassword = getSHA256(newPassword);
        }
        UpdateUserModel updateUser = new UpdateUserModel(user.getUsername(), newPassword, user.getFirstName(),
                user.getLastName(), user.getPhoneNumber(), user.getImage());
        userDao.updateUser(updateUser, id);
        return new ChangeUserInfoResponseModel(true, "Success!!");
    }

    @GetMapping("/users/{id}")
    public GetUserResponseModel getUser(@PathVariable("id") String id) {
        return userDao.getUser(id);
    }

    @GetMapping("/users")
    public List<GetUserResponseModel> getAllUsers(@RequestHeader("authorization") String id) {
        if (userDao.isAdmin(id)) {
            return userDao.getAllUsers();
        }
        return null;
    }
}
