package com.example.RentACar.controller;

import com.example.RentACar.dao.UserDao;
import com.example.RentACar.dao.UserDaoSQL;
import com.example.RentACar.model.AdminUpdateUserModel;
import com.example.RentACar.model.request.AdminChangeUserInfoRequestModel;
import com.example.RentACar.model.response.AdminChangeUserInfoResponseModel;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {
    UserDao userDao = new UserDaoSQL();

    private boolean isEmailValid(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @PatchMapping("/admin/update/{id}")
    public AdminChangeUserInfoResponseModel
    changeUserInformation(@PathVariable("id") String userId,
                          @RequestHeader("authorization") String adminId,
                          @RequestBody AdminChangeUserInfoRequestModel info){
        if (!userDao.isAdmin(adminId)){
            return new AdminChangeUserInfoResponseModel
                    (false, "Only Admin can access this option!!!");
        }

        String username = info.getUsername();
        String email = info.getEmail();

        if ((!username.isEmpty()) && username.length() < 3){
            username = "";
        }
        if ((!email.isEmpty()) && !isEmailValid(email)){
            email = "";
        }
        if (username.isEmpty() && email.isEmpty() && info.getFirst_name().isEmpty()
                && info.getLast_name().isEmpty() && info.getPersonal_number().isEmpty()
                && info.getPhone_number().isEmpty() && info.getImage().isEmpty()){
            return new AdminChangeUserInfoResponseModel(false, "Update isn't successful!!");
        }
        AdminUpdateUserModel userInfo = new AdminUpdateUserModel(username, email, info.getFirst_name(),
                info.getLast_name(), info.getPhone_number(), info.getPersonal_number(), info.getImage());

        userDao.adminUpdateUserInfo(userInfo, userId);
        return new AdminChangeUserInfoResponseModel(true, "Success!!!");
    }
}
