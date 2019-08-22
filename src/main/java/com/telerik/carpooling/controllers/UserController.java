package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.Image;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.TripDtoResponse;
import com.telerik.carpooling.models.dtos.UserDtoRequest;
import com.telerik.carpooling.models.dtos.UserDtoResponse;
import com.telerik.carpooling.models.dtos.dtos.mapper.DtoMapper;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.FeedbackService;
import com.telerik.carpooling.services.services.contracts.ImageService;
import com.telerik.carpooling.services.services.contracts.RatingService;
import com.telerik.carpooling.services.services.contracts.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

    private final UserService userService;
    private final ImageService imageService;
    private final RatingService ratingService;
    private final FeedbackService feedbackService;
    private final DtoMapper dtoMapper;

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> getUsers(@RequestParam(value = "_end", required = false)
                                              Integer pageNumber,
                                      @RequestParam(value = "_start", required = false)
                                              Integer pageSize,
                                      @RequestParam(value = "username", required = false)
                                              String username,
                                      @RequestParam(value = "firstName", required = false)
                                              String firstName,
                                      @RequestParam(value = "lastName", required = false)
                                              String lastName,
                                      @RequestParam(value = "email", required = false)
                                              String email,
                                      @RequestParam(value = "phone", required = false)
                                              String phone){

        return Optional
                .ofNullable(dtoMapper.userToDtoList(userService.getUsers(pageNumber, pageSize, username, firstName, lastName, email,
                        phone)))
                .map(userDtoResponse -> ResponseEntity.ok().body(userDtoResponse))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping (value = "/top-rated-drivers")
    public ResponseEntity<?> getTopRatedDrivers(@RequestParam(value = "_end", required = false)
                                                            Integer pageNumber,
                                                @RequestParam(value = "_start", required = false)
                                                            Integer pageSize,
                                                @RequestParam(value = "username", required = false)
                                                            String username,
                                                @RequestParam(value = "firstName", required = false)
                                                            String firstName,
                                                @RequestParam(value = "lastName", required = false)
                                                            String lastName,
                                                @RequestParam(value = "email", required = false)
                                                            String email,
                                                @RequestParam(value = "phone", required = false)
                                                            String phone) {
        return Optional
                .ofNullable(dtoMapper.userToDtoList(userService.getTopRatedDrivers(pageNumber, pageSize, username, firstName, lastName, email,
                        phone)))
                .map(k -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @GetMapping (value = "/top-rated-passengers")
    public ResponseEntity<?> getTopRatedPassengers(@RequestParam(value = "_end", required = false)
            Integer pageNumber,
                                                   @RequestParam(value = "_start", required = false)
                                                               Integer pageSize,
                                                   @RequestParam(value = "username", required = false)
                                                               String username,
                                                   @RequestParam(value = "firstName", required = false)
                                                               String firstName,
                                                   @RequestParam(value = "lastName", required = false)
                                                               String lastName,
                                                   @RequestParam(value = "email", required = false)
                                                               String email,
                                                   @RequestParam(value = "phone", required = false)
                                                               String phone) {

        return Optional
                .ofNullable(dtoMapper.userToDtoList(userService.getTopRatedPassengers(pageNumber, pageSize, username, firstName, lastName, email,
                        phone)))
                .map(k -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> editUser(@Valid @RequestBody UserDtoResponse userDtoResponse){
        User user =dtoMapper.dtoToObject(userDtoResponse);

        return Optional
                .ofNullable( userService.updateUser(user))
                .map(k -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping (value = "/{username}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> getUser(@PathVariable() String username){
        return Optional
                .ofNullable(dtoMapper.objectToDto(userService.getUser(username)))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/me")
    public ResponseEntity<?> getUserOwnInfo(Authentication authentication) {
        return Optional
                .ofNullable(dtoMapper.objectToDto(userService.getUser(
                        authentication.getName())))
                .map(user -> ResponseEntity.ok().body(user))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PatchMapping(value = "/{userId}/delete")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<?> deleteUser(@PathVariable final String userId) {
        return Optional
                .ofNullable(userService.deleteUser(userId))
                .map(k -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping(value = "/me/update-password")
    public ResponseEntity<?> updateUserOwnInfo(@RequestParam final String password, final Authentication authentication) {

        return Optional
                .ofNullable(userService.updateCurrentUserPassword(password, userService.getUser(
                        authentication.getName())))
                .map(user -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());

    }

    @PatchMapping(value = "/me/update-email")
    public ResponseEntity<?> updateUserOwnEmail(@RequestParam final String email, final Authentication authentication) {
        return Optional
                .ofNullable(userService.updateCurrentUserEmail(email, userService.getUser(
                        authentication.getName())))
                .map(user -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping(value = "/register")
    public ResponseEntity<?> save(@Valid @RequestBody final UserDtoRequest userDtoRequest) {
        User user = dtoMapper.dtoToObject(userDtoRequest);
        return Optional
                .ofNullable(userService.save(user))
                .map(userDtoResponse -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/avatar")
    public ResponseEntity<?> uploadUserImage(@RequestParam("upfile") final MultipartFile file,
                                                final Authentication authentication) {

        URI fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();

        return Optional
                .ofNullable(imageService.storeUserImage(file,
                        userService.getUser(authentication.getName()),fileDownloadUri))
                .map(userDtoResponse -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/avatar/car")
    public ResponseEntity<?> uploadCarImage(@RequestParam("upfile") final MultipartFile file,
                                               final Authentication authentication) {

        URI fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUri();
        return Optional
                .ofNullable(imageService.storeCarImage(file,
                        userService.getUser(authentication.getName()),fileDownloadUri))
                .map(userDtoResponse -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/avatarMe")
    public ResponseEntity<byte[]> downloadUserOwnImage(final Authentication authentication) {

        return Optional
                .ofNullable(imageService.getImage(userService.getUser(
                        authentication.getName()).getUserImage().getModelId()))
                .map(this::createImageModelInResponseEntity)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/avatar/{username}")
    public ResponseEntity<byte[]> downloadUserImage(@PathVariable String username) {

            return Optional
                    .ofNullable(imageService.getImage(userService.getUser(username).getUserImage().getModelId()))
                    .map(this::createImageModelInResponseEntity)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/avatarMe/car")
    public ResponseEntity<byte[]> downloadOwnCarImage(Authentication authentication) {

        return Optional
                .ofNullable(imageService.getImage(userService.getUser(
                        authentication.getName()).getCar().getCarImage().getModelId()))
                .map(this::createImageModelInResponseEntity)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/avatar/car/{username}")
    public ResponseEntity<byte[]> downloadCarImage(@PathVariable String username) {

            return Optional
                    .ofNullable(imageService.getImage(userService.getUser(username).getCar().getCarImage().getModelId()))
                    .map(this::createImageModelInResponseEntity)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/{tripId}/users/{userID}/rate")
    public ResponseEntity<?> rateUser(@PathVariable final String tripId,
                                      @PathVariable final String userID,
                                      final Authentication authentication,
                                      @RequestBody Integer rating) {

        return Optional
                .ofNullable(ratingService.rateUser(tripId, userService.getUser(
                        authentication.getName()), userID, rating))
                .map(tripDtoResponse -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping(value = "/{tripId}/users/{userID}/feedback")
    public ResponseEntity<?> leaveFeedback(@PathVariable final String tripId,
                                           @PathVariable final String userID,
                                           final Authentication authentication,
                                           @RequestBody String feedback) {

        return Optional
                .ofNullable(feedbackService.leaveFeedback(tripId, userService.getUser(
                        authentication.getName()), userID, feedback))
                .map(tripDtoResponse -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/{username}/feedback")
    public ResponseEntity<?>getFeedback(@PathVariable final String username){
        return Optional
                .ofNullable(feedbackService.getFeedback(username))
                .map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }


    private ResponseEntity<byte[]> createImageModelInResponseEntity(Image dbFile) {
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf(dbFile.getContentType()));
        header.setContentLength(dbFile.getData().length);
        header.set("Content-Disposition", "attachment; filename=" + dbFile.getFileName());
        return new ResponseEntity<>(dbFile.getData(), header, HttpStatus.OK);
    }
}
