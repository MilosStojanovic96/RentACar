package com.example.RentACar.controller;

import com.example.RentACar.dao.*;
import com.example.RentACar.model.request.contract.ContractApprovalRequestModel;
import com.example.RentACar.model.request.contract.ContractSampleRequestModel;
import com.example.RentACar.model.request.contract.SignedContractRequestModel;
import com.example.RentACar.model.response.contract.ContractResponseModel;
import com.example.RentACar.model.response.contract.ContractSampleResponseModel;
import com.example.RentACar.model.response.contract.SignedContractResponseModel;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import static java.time.temporal.ChronoUnit.DAYS;

public class ContractController {
    private static ContractDao contractDao = new ContractDaoSQL();
    private static CarDao carDao = new CarDaoSQL();
    private static UserDao userDao = new UserDaoSQL();

    private static double getContractPrice(LocalDate startDate, LocalDate endDate, String carId) {
        double price = carDao.getPrice(carId);
        int days = (int) DAYS.between(startDate, endDate) + 1;
        return price * days;
    }

    @PostMapping("/contracts/sample")
    public ContractSampleResponseModel getContractSample(
            @RequestBody ContractSampleRequestModel conSample) {
        double contractPrice = getContractPrice(conSample.getStartDate(),
                conSample.getEndDate(), conSample.getCarId());

        return new ContractSampleResponseModel(conSample.getUserId(), conSample.getCarId(),
                conSample.getStartDate(), conSample.getEndDate(), contractPrice, false);
    }

    @PostMapping("/contracts")
    public SignedContractResponseModel postSingedContract
            (@RequestBody SignedContractRequestModel contract){
        if (contractDao.userHasPendingContract(contract.getUserId())){
            return new SignedContractResponseModel(false, "User already has pending contract!!");
        }
        if (!carDao.isCarAvailable(contract.getStartDate(), contract.getEndDate(), contract.getCarId())){
            return new SignedContractResponseModel(false, "Car is not available for whole duration of the contract!!");
        }
        contractDao.addContractToDB(contract);
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
    public List<ContractResponseModel> getContractHistory(@PathVariable("userId") String id) {
        return contractDao.getContractHistory(id);
    }

    @PostMapping("/contracts/{contractId}/approval")
    public void approveContract(@RequestHeader("authorization") String adminId,
                                @PathVariable("contractId") String contractId,
                                @RequestBody ContractApprovalRequestModel adminApproval){

        if (!userDao.isAdmin(adminId)){
            return;
        }
        if (adminApproval.isApproved()){
            contractDao.updateContractApproval(contractId, true);
        }
        else
            contractDao.deleteContract(contractId);
    }
}
