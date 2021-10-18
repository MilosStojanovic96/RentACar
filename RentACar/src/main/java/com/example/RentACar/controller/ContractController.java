package com.example.RentACar.controller;

import com.example.RentACar.dao.*;
import com.example.RentACar.model.request.contract.ContractApprovalRequestModel;
import com.example.RentACar.model.request.contract.ContractSampleRequestModel;
import com.example.RentACar.model.request.contract.SignedContractRequestModel;
import com.example.RentACar.model.response.contract.ContractResponseModel;
import com.example.RentACar.model.response.contract.ContractSampleResponseModel;
import com.example.RentACar.model.response.contract.SignedContractResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import static java.time.temporal.ChronoUnit.DAYS;

@RestController
public class ContractController {
    private static ContractDao contractDao = new ContractDaoSQL();
    private static CarDao carDao = new CarDaoSQL();
    private static UserDao userDao = new UserDaoSQL();

    private static double getContractPrice(LocalDate startDate, LocalDate endDate, String carId) {
        double price = carDao.getPrice(carId);
        int days = (int) DAYS.between(startDate, endDate) + 1;
        return price * days;
    }

    // `/contracts/sample` - *POST*
    //   - Враћа нацрт уговора који клијент може да потпише и пошаље на одобравање
    @PostMapping("/contracts/sample")
    public ContractSampleResponseModel getContractSample(
            @RequestBody ContractSampleRequestModel conSample) {
        double contractPrice = getContractPrice(conSample.getStartDate(),
                conSample.getEndDate(), conSample.getCarId());

        return new ContractSampleResponseModel(conSample.getUserId(), conSample.getCarId(),
                conSample.getStartDate(), conSample.getEndDate(), contractPrice, false);
    }


    // `/contracts` - *POST*
    //    - Додаје нови уговор (Клијент)
    //    - Ово је нека врста резервације датума за клијента
    //    - Након овога, администратор или одбија или одобрава
    //        - Одбијање уговора значи брисање из базе (описано касније)
    //    - Клијент може да има највише један уговор на чекању
    @PostMapping("/contracts")
    public SignedContractResponseModel postSingedContract
            (@RequestBody SignedContractRequestModel contract){
        if (contractDao.userHasPendingContract(contract.getUserId())){
            return new SignedContractResponseModel(false, "User already has pending contract.");
        }
        if (!carDao.isCarAvailable(contract.getStartDate(), contract.getEndDate(), contract.getCarId())){
            return new SignedContractResponseModel(false, "Car is not available for whole duration of the contract.");
        }
        contractDao.addContractToDB(contract);
        return new SignedContractResponseModel(true, "Contract created, waiting for approval.");
    }

    //`/contracts` - *GET*
    //    - Администратори могу да виде све уговоре
    //    - Кроз header се прослеђује id админа
    @GetMapping("/contracts")
    public List<ContractResponseModel> getAllcontracts(@RequestHeader("authorization") String adminId) {
        if (!userDao.isAdmin(adminId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return contractDao.getAllContracts();
    }

    // `/contracts/pending` - *GET*
    //    - Администратори могу да виде све неразрешене уговоре
    //    - Кроз header се прослеђује id админа
    //    - Враћа исто као пример изнад, али само уговоре који имају approved постављено на false
    @GetMapping("/contracts/pending")
    public List<ContractResponseModel> getAllPendingContracts
            (@RequestHeader("authorization") String adminId) {
        if (!userDao.isAdmin(adminId)) {
            return null;
        }
        return contractDao.getAllPendingContracts();
    }


    //`/contracts/{contractId}/approval` - *POST*
    //    - Администратор одобрава или одбија уговор
    //        - Ако га одбије - уговор се брише из базе
    //        - Ако га одобри - Уговор добија approved = true
    //    - Кроз header се прослеђује id админа
    @PostMapping("/contracts/{contractId}/approval")
    public void approveContract(@RequestHeader("authorization") String adminId,
                                @PathVariable("contractId") String contractId,
                                @RequestBody ContractApprovalRequestModel adminApproval){

        if (!userDao.isAdmin(adminId)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (adminApproval.isApproved()){
            contractDao.updateContractApproval(contractId, true);
        }
        else
            contractDao.deleteContract(contractId);
    }


    //`/contracts/{userId}/history` - *GET*
    //    - Дохвата се историја уговора за клијента са датим id
    @GetMapping("/contracts/{userId}/history")
    public List<ContractResponseModel> getContractHistory(@PathVariable("userId") String id) {
        return contractDao.getContractHistory(id);
    }
}
