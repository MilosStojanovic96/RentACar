package com.example.RentACar.controller;

import com.example.RentACar.dao.*;
import com.example.RentACar.model.SearchCarModel;
import com.example.RentACar.model.request.car.AddCarRequestModel;
import com.example.RentACar.model.request.car.ChangeCarInfoRequestModel;
import com.example.RentACar.model.response.car.GetCarResponseModel;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class CarController {
    private CarDao carDao = new CarDaoSQL();
    private UserDao userDao = new UserDaoSQL();
    private ContractDao contractDao = new ContractDaoSQL();

    //1. /cars - GET
    //Враћа све аутомобилe
    @GetMapping("/cars")
    public List<GetCarResponseModel> getAllCars(){
        return carDao.getAllCars();
    }

    //2. /cars/search? - GET
    @GetMapping("/cars/search")
    public List<GetCarResponseModel> searchCars(@RequestParam(required = false) String make,
                                                @RequestParam(required = false) String model,
                                                @RequestParam(required = false) Integer year,
                                                @RequestParam(required = false) Boolean automatic,
                                                @RequestParam(required = false) Double price,
                                                @RequestParam(required = false) Integer power,
                                                @RequestParam(required = false) Integer doors){
        return carDao.searchCars(new SearchCarModel(make, model, year, power, doors, price, automatic),
                carDao.getAllCars());
    }

    //3. /cars/{carId} - GET
    //Дохвата информације о једном аутомобилу
    @GetMapping("/cars/{carId}")
    public GetCarResponseModel getCar(@PathVariable("carId") String id){
        return carDao.getCar(id);
    }

    //4. /cars/{carId} - PATCH
    //Мења аутомобил (Ово може да ради само администратор)
    @PatchMapping("/cars/{carId}")
    public void updateCar(@PathVariable("carId") String carId,
                          @RequestHeader("authorization") String adminId,
                          @RequestBody ChangeCarInfoRequestModel carInfo){
        if (!userDao.isAdmin(adminId)){
            return;
        }
        carDao.updateCarInfo(carId, carInfo);
    }

    //5. /cars/{carId} - DELETE
    //Брише аутомобил са датим id - (Само админ)
    @DeleteMapping("/cars/{carId}")
    public void deleteCar(@PathVariable("carId") String carId,
                          @RequestHeader("authorization") String adminId){
        if (!userDao.isAdmin(adminId)){
            return;
        }
        carDao.delete(carId);
    }

    //6. /cars - POST
    //Додаје нови аутомобил - (Само админ)
    @PostMapping("/cars")
    public void addCar(@RequestHeader("authorization") String adminId,
                       @RequestBody AddCarRequestModel car){
        if (!userDao.isAdmin(adminId)){
            return;
        }
        carDao.addCar(car);
    }

    //7. /cars/available? - GET
    //Дохвата све доступне аутомобиле
    //startDate и endDate се прослеђују као query параметри
    @GetMapping("/cars/available")
    public List<GetCarResponseModel> availableCars(@RequestParam String startDate,
                                                   @RequestParam String endDate){
        LocalDate startDateLocal = LocalDate.parse(startDate);
        LocalDate endDateLocal = LocalDate.parse(endDate);
        return carDao.getAvailableCars(startDateLocal, endDateLocal);
    }

    //8. /cars/available/search? - GET
    //Дохвата све доступне аутомобиле
    //startDate и endDate се прослеђују као query параметри (обавезни)
    //Query параметри који нису обавезни:name - string, year - int, make - string...
    @GetMapping("/cars/available/search")
    public List<GetCarResponseModel> searchCars(@RequestParam String startDate,
                                                @RequestParam String endDate,
                                                @RequestParam(required = false) String make,
                                                @RequestParam(required = false) String model,
                                                @RequestParam(required = false) Integer year,
                                                @RequestParam(required = false) Boolean automatic,
                                                @RequestParam(required = false) Double price,
                                                @RequestParam(required = false) Integer power,
                                                @RequestParam(required = false) Integer doors){
        LocalDate startDateLocal = LocalDate.parse(startDate);
        LocalDate endDateLocal = LocalDate.parse(endDate);
        return carDao.searchCars(new SearchCarModel(make, model, year, power, doors, price, automatic),
                carDao.getAvailableCars(startDateLocal, endDateLocal));

    }

    //9. /cars/{carId}/calendar - GET
    //Враћа листу свих датума који нису доступни за одређени аутомобил
    //Ову информацију имамо у уговорима
    //Чак и ако нису одобрени уговори, те датуме сматрамо заузетим
    @GetMapping("/cars/{carId}/calendar")
    public List<LocalDate> getCarOccupied(@PathVariable("carId") String id){
        return contractDao.getCarOccupiedDates(id);
    }
}
