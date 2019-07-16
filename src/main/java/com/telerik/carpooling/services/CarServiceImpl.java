package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.repositories.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    
    private final CarRepository carRepository;

    @Override
    public Car createCar(Car car) {
        return null;
    }

    @Override
    public Car updateCurrentBrand(String brand, Car car) {
        return null;
    }

    @Override
    public Car updateCurrentModel(String model, Car car) {
        return null;
    }

    @Override
    public Car updateCurrentColor(String color, Car car) {
        return null;
    }

    @Override
    public Car updateCurrentTotalSeats(int totalSeats, Car car) {
        return null;
    }

    @Override
    public Car updateCurrentFirstRegistration(int firstRegistration, Car car) {
        return null;
    }

    @Override
    public Car updateIsAirConditioned(boolean isAirConditioned, Car car) {
        return null;
    }

    @Override
    public Car updateIsSmokingAllowed(boolean isSmokingAllowed, Car car) {
        return null;
    }

    @Override
    public Car updateIsLuggageAllowed(boolean isLuggageAllowed, Car car) {
        return null;
    }

    @Override
    public Car updateIsPetsAllowed(boolean isPetsAllowed, Car car) {
        return null;
    }

    @Override
    public Car updateIsReturnJourney(boolean isReturnJourney, Car car) {
        return null;
    }

    @Override
    public Car updateAreChildrenUnder7Allowed(boolean AreChildrenUnder7Allowed, Car car) {
        return null;
    }
}
