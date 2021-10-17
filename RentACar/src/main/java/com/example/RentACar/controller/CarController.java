package com.example.RentACar.controller;

import com.example.RentACar.dao.*;
import com.example.RentACar.model.CarsModel;
import com.example.RentACar.model.SearchCarModel;
import com.example.RentACar.model.request.ChangeCarInfoRequestModel;
import com.example.RentACar.model.response.GetCarResponseModel;
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
        //Query параметри:
        //name - string
        //year - int
        //make - string
        //model - string
        //automatic - boolean
        //price - double
        //power - int
        //doors - int

    @GetMapping("/cars/search")
    public List<GetCarResponseModel> searchCars(@RequestParam(required = false) int year,
                                                @RequestParam(required = false) String make,
                                                @RequestParam(required = false) String model,
                                                @RequestParam(required = false) boolean automatic,
                                                @RequestParam(required = false) double price,
                                                @RequestParam(required = false) int power,
                                                @RequestParam(required = false) int doors){
        return carDao.searchCars(new SearchCarModel(year, make, model, automatic, price, power, doors),
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
        //Кроз header се прослеђује id админа - https://www.baeldung.com/spring-rest-http-headers

    @PatchMapping("cars/{carId}")
    public void updateCar (@PathVariable("carId") String car_id,
                           @RequestHeader("authorization") String admin_id,
                           @RequestBody ChangeCarInfoRequestModel carInfo){
        if (!userDao.isAdmin(admin_id)){
            return;
        }
        carDao.updateCarInfo(car_id, carInfo);
    }

        //5. /cars/{carId} - DELETE
        //Брише аутомобил са датим id - (Само админ)
        //Кроз header се прослеђује id админа - https://www.baeldung.com/spring-rest-http-headers

    @DeleteMapping("/cars/{carId}")
    public void deleteCar (@PathVariable("carId") String car_id,
                           @RequestHeader("authorization") String admin_id){
        if (!userDao.isAdmin(admin_id)){
            return;
        }
        carDao.delete(car_id);
    }

        //6. /cars - POST
        //Додаје нови аутомобил - (Само админ)
        //Кроз header се прослеђује id админа - https://www.baeldung.com/spring-rest-http-headers

    @PostMapping("/cars")
    public void addCar(@RequestHeader("authorization") String admin_id,
                       @RequestBody CarsModel car){
        if (!userDao.isAdmin(admin_id)){
            return;
        }
        carDao.addCar(car);
    }


        //7. /cars/available? - GET
        //Дохвата све доступне аутомобиле
        //startDate и endDate се прослеђују као query параметри

    @GetMapping("/cars/available")
    public List<GetCarResponseModel> availableCars(@RequestParam String start,
                                                   @RequestParam String end){
        LocalDate start_date = LocalDate.parse(start);
        LocalDate end_date = LocalDate.parse(end);
        return carDao.getAvailableCars(start_date,end_date);
    }

        //8. /cars/available/search? - GET
        //Дохвата све доступне аутомобиле
        //startDate и endDate се прослеђују као query параметри (обавезни)
        //Query параметри који нису обавезни:
        //name - string
        //year - int
        //make - string
        //model - string
        //automatic - boolean
        //price - double
        //power - int
        //doors - int

    @GetMapping("/cars/available/search")
    public List<GetCarResponseModel> searchCars (@RequestParam String start,
                                                 @RequestParam String end,
                                                 @RequestParam(required = false) int year,
                                                 @RequestParam(required = false) String make,
                                                 @RequestParam(required = false) String model,
                                                 @RequestParam(required = false) boolean automatic,
                                                 @RequestParam(required = false) double price,
                                                 @RequestParam(required = false) int power,
                                                 @RequestParam(required = false) int doors){
        LocalDate start_date = LocalDate.parse(start);
        LocalDate end_date = LocalDate.parse(end);
        return carDao.searchCars(new SearchCarModel(year, make, model, automatic, price, power, doors),
                carDao.getAvailableCars(start_date,end_date));
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
