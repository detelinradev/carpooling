package com.telerik.carpooling.services;

import com.telerik.carpooling.exceptions.FileStorageException;
import com.telerik.carpooling.exceptions.MyNotFoundException;
import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.Image;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.repositories.CarRepository;
import com.telerik.carpooling.repositories.ImageRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    public void storeUserImage(MultipartFile file, String loggedUserUsername) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        User user = findUserByUsername(loggedUserUsername);

        try {
            if (fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Image image = new Image(file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes(), user);
            image.setIsDeleted(false);
            imageRepository.save(image);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public void storeCarImage( MultipartFile file,String loggedUserUsername) {

        User user = findUserByUsername(loggedUserUsername);
        Optional<Car> car = carRepository.findByOwnerAndIsDeletedFalse(user);
        if(car.isPresent()) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                if (fileName.contains("..")) {
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                }
                Image image = new Image(file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes(), car.get());
                image.setIsDeleted(false);
                imageRepository.save(image);
            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
            }
        }
    }

    @Override
    public Image getUserImage(String username) {
        User user = findUserByUsername(username);
        return imageRepository.findByUserAndIsDeletedFalse(user)
                .orElseThrow(() -> new MyNotFoundException("User image not found for user " + user.getUsername()));
    }

    @Override
    public Image getCarImage(String username) throws MyNotFoundException {
        User user = findUserByUsername(username);
        Car car = findCarByUser(user);

        return imageRepository.findByCarAndIsDeletedFalse(car)
                .orElseThrow(() -> new MyNotFoundException("Car image not found for car owned of " + user.getUsername()));
    }

    @Override
    public void deleteUserImage(String username, Authentication authentication) {

        User user = findUserByUsername(username);
        User loggedUser = findUserByUsername(authentication.getName());
        if (isRole_AdminOrSameUser(authentication, user, loggedUser)) {
            Image image = getUserImage(username);
            image.setIsDeleted(true);
            imageRepository.save(image);
        }else throw new IllegalArgumentException("You are not authorized to delete the image");
    }

    @Override
    public void deleteCarImage(String username, Authentication authentication) throws MyNotFoundException {
        User user = findUserByUsername(username);
        User loggedUser = findUserByUsername(authentication.getName());
        if (isRole_AdminOrSameUser(authentication, user, loggedUser)) {
            Image image = getCarImage(username);
            image.setIsDeleted(true);
            imageRepository.save(image);
        }else throw new IllegalArgumentException("You are not authorized to delete the image");
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
    }

    private Car findCarByUser(User user) throws MyNotFoundException {
        return carRepository.findByOwnerAndIsDeletedFalse(user)
                .orElseThrow(() -> new MyNotFoundException("User do not have a car"));
    }

    private boolean isRole_AdminOrSameUser(Authentication authentication, User user, User loggedUser) {

        return loggedUser.equals(user) || authentication.getAuthorities()
                .stream()
                .anyMatch(k -> k.getAuthority().equals("ROLE_ADMIN"));
    }
}
