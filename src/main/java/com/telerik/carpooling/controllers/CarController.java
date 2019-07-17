package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CarDto;
import com.telerik.carpooling.models.dtos.UserDto;
import com.telerik.carpooling.repositories.CarRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.security.AuthenticationService;
import com.telerik.carpooling.services.CarService;
import com.telerik.carpooling.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@RequestMapping("api/car")
public class CarController {

    private final CarRepository carRepository;
    private final AuthenticationService authenticationService;
    private final CarService carService;
//
//    @GetMapping(value = "api/me")
//    public ResponseEntity<User> getUserOwnInfo(final HttpServletRequest req) {
//
//        return Optional
//                .ofNullable(userRepository.findFirstByUsername(
//                        authenticationService.getUsername(req)))
//                .map(user -> ResponseEntity.ok().body(user))
//                .orElseGet(() -> ResponseEntity.notFound().build());
//
//    }
//
//    @PatchMapping(value = "api/me/update-password")
//    public ResponseEntity<User> updateUserOwnInfo(@RequestParam final String password, final HttpServletRequest req) {
//
//        return Optional
//                .ofNullable(userService.updateCurrentUserPassword(password, userRepository.findFirstByUsername(
//                        authenticationService.getUsername(req))))
//                .map(user -> ResponseEntity.ok().body(user))
//                .orElseGet(() -> ResponseEntity.badRequest().build());
//
//    }
//
//    @PatchMapping(value = "api/me/update-email")
//    public ResponseEntity<User> updateUserOwnEmail(@RequestParam final String email, final HttpServletRequest req) {
//        return Optional
//                .ofNullable(userService.updateCurrentUserEmail(email, userRepository.findFirstByUsername(
//                        authenticationService.getUsername(req))))
//                .map(user -> ResponseEntity.ok().body(user))
//                .orElseGet(() -> ResponseEntity.badRequest().build());
//    }
//
//    @PutMapping(value = "/car")
//    public ResponseEntity<Car> save(@Valid @RequestBody final CarDto car) {
//        return new ResponseEntity<>(carService.save(car), HttpStatus.OK);
//    }

}
