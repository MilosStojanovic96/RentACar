package com.example.RentACar.controller;

import com.example.RentACar.dao.*;
import com.example.RentACar.model.request.ContractApprovalRequestModel;
import com.example.RentACar.model.request.ContractSampleRequestModel;
import com.example.RentACar.model.request.SignedContractRequestModel;
import com.example.RentACar.model.response.ContractResponseModel;
import com.example.RentACar.model.response.ContractSampleResponseModel;
import com.example.RentACar.model.response.SignedContractResponseModel;
import org.springframework.web.bind.annotation.*;
import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.util.List;
import static java.time.temporal.ChronoUnit.DAYS;

public class ContractController {
    private static ContractDao contractDao = new ContractDaoSQL();
    private static CarDao carDao = new CarDaoSQL();
    private static UserDao userDao = new UserDaoSQL();
    private static double getContractPrice(LocalDate start_date, LocalDate end_date, String car_id){
        double price = carDao.getPrice(car_id);
        int days = (int) (DAYS.between(start_date, end_date) + 1);
        return price * days;
    }
    @PostMapping("/contracts/sample")
    public ContractSampleResponseModel getContractSample(
            @RequestBody ContractSampleRequestModel conSample){
        double contractPrice = getContractPrice(conSample.getStart_date(),
                conSample.getEnd_date(),conSample.getCar_id());
        return  new ContractSampleResponseModel(conSample.getUser_id(),
                conSample.getCar_id(),conSample.getStart_date(),conSample.getEnd_date(),
                contractPrice, false);
    }
    @PostMapping("/contracts")
    public SignedContractResponseModel postSingedContract
            (@RequestBody SignedContractRequestModel contract){
        if (contractDao.userHasPendingContract(contract.getUser_id())){
            return new SignedContractResponseModel(false, "User already has pending contract!!");
        }
        if (!carDao.isCarAvailable(contract.getStart_date(), contract.getEnd_date(), contract.getCar_id())){
            return new SignedContractResponseModel(false, "Car is not available for whole duration of the contract!!");
        }
        contractDao.addContractToDatabase(contract);
        return new SignedContractResponseModel(true, "Contract created, waiting for approval!!");
    }
    @GetMapping("/contracts")
    public List<ContractResponseModel> getAllcontracts(@RequestHeader("authorization") String adminId) {
        if (!userDao.isAdmin(adminId)) {
            return null;
        }
        return contractDao.getAllContracts();
    }
    @GetMapping("/contracts/pending")
    public List<ContractResponseModel> getAllPendingContracts
            (@RequestHeader("authorization") String adminId) {
        if (!userDao.isAdmin(adminId)) {
            return null;
        }
        return contractDao.getAllPendingContracts();
    }
    @GetMapping("/contracts/{userId}/history")
    public List<ContractResponseModel> getContractHistory(@PathParam("userId") String userId) {
        return contractDao.getContractHistory(userId);
    }
    @PostMapping("/contracts/{contractId}/approval")
    public void approveContract(@PathParam("contractId") String contractId,
                                @RequestHeader("authorization") String adminId,
                                @RequestBody ContractApprovalRequestModel adminApproval){
        if (!userDao.isAdmin(adminId)){
            return;
        }
        if (adminApproval.isApproved()){
            contractDao.updateContractApproval(contractId, true);
        }
        else {
            contractDao.deleteContract(contractId);
        }
    }
}
