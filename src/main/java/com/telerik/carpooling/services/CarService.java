package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Car;

public interface CarService {
    Car createCar(Car car);

    Car updateCurrentBrand(String brand, Car car);

    Car updateCurrentModel(String model, Car car);

    Car updateCurrentColor(String color, Car car);

    Car updateCurrentTotalSeats(int totalSeats, Car car);

    Car updateCurrentFirstRegistration(int firstRegistration, Car car);

    Car updateIsAirConditioned(boolean isAirConditioned, Car car);

    Car updateIsSmokingAllowed(boolean isSmokingAllowed, Car car);

    Car updateIsLuggageAllowed(boolean isLuggageAllowed, Car car);

    Car updateIsPetsAllowed(boolean isPetsAllowed, Car car);

    Car updateIsReturnJourney(boolean isReturnJourney, Car car);

    Car updateAreChildrenUnder7Allowed(boolean AreChildrenUnder7Allowed, Car car);
}
