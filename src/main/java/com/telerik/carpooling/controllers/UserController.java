package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.Image;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoResponse;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.ImageService;
import com.telerik.carpooling.services.services.contracts.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping(value = "users")
public class UserController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<List<UserDtoResponse>> getUsers (){
        return Optional
                .ofNullable(userService.getUsers())
                .map(users -> ResponseEntity.ok().body(users))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping (value = "/top-rated-drivers")
    public ResponseEntity<List<UserDtoResponse>> getTopRatedDrivers() {
        List<UserDtoResponse> users = userService.getUsers();
        users.sort((a, b) -> b.getRatingAsDriver().compareTo(a.getRatingAsDriver()));

        List<UserDtoResponse>finalUserList =users.stream()
                .limit(10)
                .collect(Collectors.toList());

        return Optional
                .of(finalUserList)
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping (value = "/top-rated-passengers")
    public ResponseEntity<List<UserDtoResponse>> getTopRatedPassengers() {
        List<UserDtoResponse> users = userService.getUsers();
        users.sort((a, b) -> b.getRatingAsPassenger().compareTo(a.getRatingAsPassenger()));

        List<UserDtoResponse>finalUserList =users.stream()
                .limit(10)
                .collect(Collectors.toList());

        return Optional
                .of(finalUserList)
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    //@PreAuthorize(value = "hasRole(ADMIN)")
    public ResponseEntity<?> editUser(@Valid @RequestBody UserDtoResponse userDtoResponse){
        return Optional
                .ofNullable(userService.updateUser(userDtoResponse))
                .map(user -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping (value = "/{username}")
    public ResponseEntity<?> getUser(@PathVariable() String username){
        return Optional
                .ofNullable(userService.getUser(username))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/me")
    public ResponseEntity<?> getUserOwnInfo(Authentication authentication) {
        return Optional
                .ofNullable(userService.getUser(
                        authentication.getName()))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PatchMapping(value = "/me/update-password")
    public ResponseEntity<User> updateUserOwnInfo(@RequestParam final String password, final Authentication authentication) {

        return Optional
                .ofNullable(userService.updateCurrentUserPassword(password, userRepository.findFirstByUsername(
                        authentication.getName())))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.badRequest().build());

    }

    @PatchMapping(value = "/me/update-email")
    public ResponseEntity<User> updateUserOwnEmail(@RequestParam final String email, final Authentication authentication) {
        return Optional
                .ofNullable(userService.updateCurrentUserEmail(email, userRepository.findFirstByUsername(
                        authentication.getName())))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserDtoResponse> save(@Valid @RequestBody final UserDtoRequest user) {
        return Optional
                .ofNullable(userService.save(user))
                .map(userDtoResponse -> ResponseEntity.ok().body(userDtoResponse))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/avatar")
    public ResponseEntity<?> uploadUserImage(@RequestParam("upfile") final MultipartFile file,
                                                final Authentication authentication) {

        URI fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();

        return Optional
                .ofNullable(imageService.storeUserImage(file,
                        userRepository.findFirstByUsername(authentication.getName()),fileDownloadUri))
                .map(userDtoResponse -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/avatar/car")
    public ResponseEntity<?> uploadCarImage(@RequestParam("upfile") final MultipartFile file,
                                               final Authentication authentication) {

        URI fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();
        System.out.println(1);
        return Optional
                .ofNullable(imageService.storeCarImage(file,
                        userRepository.findFirstByUsername(authentication.getName()),fileDownloadUri))
                .map(userDtoResponse -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/avatarMe")
    public ResponseEntity<byte[]> downloadUserOwnImage(final Authentication authentication) {

        return Optional
                .ofNullable(imageService.getImage(userRepository.findFirstByUsername(
                        authentication.getName()).getUserImage().getModelId()))
                .map(this::createImageModelInResponseEntity)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/avatar/{userId}")
    public ResponseEntity<byte[]> downloadUserImage(@PathVariable Long userId) {

        if(userRepository.findById(userId).isPresent()) {
            return Optional
                    .ofNullable(imageService.getImage(userRepository.findById(userId).get().getUserImage().getModelId()))
                    .map(this::createImageModelInResponseEntity)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/avatarMe/car")
    public ResponseEntity<byte[]> downloadOwnCarImage(Authentication authentication) {

        return Optional
                .ofNullable(imageService.getImage(userRepository.findFirstByUsername(
                        authentication.getName()).getCar().getCarImage().getModelId()))
                .map(this::createImageModelInResponseEntity)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/avatar/car/{userId}")
    public ResponseEntity<byte[]> downloadCarImage(@PathVariable Long userId) {

        if(userRepository.findById(userId).isPresent()) {
            return Optional
                    .ofNullable(imageService.getImage(userRepository.findById(userId).get().getCar().getCarImage().getModelId()))
                    .map(this::createImageModelInResponseEntity)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        }else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    private ResponseEntity<byte[]> createImageModelInResponseEntity(Image dbFile) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf(dbFile.getContentType()));
        header.setContentLength(dbFile.getData().length);
        header.set("Content-Disposition", "attachment; filename=" + dbFile.getFileName());
        return new ResponseEntity<>(dbFile.getData(), header, HttpStatus.OK);
    }
}
