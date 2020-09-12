package com.telerik.carpooling.service;

import com.telerik.carpooling.enums.UserRole;
import com.telerik.carpooling.exception.FileStorageException;
import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Car;
import com.telerik.carpooling.model.Image;
import com.telerik.carpooling.model.User;
import com.telerik.carpooling.repository.CarRepository;
import com.telerik.carpooling.repository.ImageRepository;
import com.telerik.carpooling.repository.UserRepository;
import com.telerik.carpooling.service.service.contract.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;

    @Transactional
    @Override
    public void storeUserImage(final MultipartFile file,final String loggedUserUsername) {

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        User user = findUserByUsername(loggedUserUsername);

        try {

            Image image = new Image(fileName,
                    file.getContentType(),
                    file.getBytes(), user);

            imageRepository.save(image);

        } catch (IOException ex) {

            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    @Transactional
    @Override
    public void storeCarImage( MultipartFile file,String loggedUserUsername) {

        Optional<Car> car = carRepository.findByOwnerAndIsDeletedFalse(loggedUserUsername);

        if(car.isPresent()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            try {

                Image image = new Image(fileName,
                        file.getContentType(),
                        file.getBytes(), car.get());

                imageRepository.save(image);
            } catch (IOException ex) {

                throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
            }
        }
    }

    @Override
    public Image getUserImage(String username) throws MyNotFoundException {

        User user = findUserByUsername(username);

        return imageRepository.findByUserAndIsDeletedFalse(user)
                .orElseThrow(() -> new MyNotFoundException("User image not found for user " + user.getUsername()));
    }

    @Override
    public Image getCarImage(String username) throws MyNotFoundException {

        User user = findUserByUsername(username);
        Car car = findCarByUser(username);

        return imageRepository.findByCarAndIsDeletedFalse(car)
                .orElseThrow(() -> new MyNotFoundException("Car image not found for car owned of " + user.getUsername()));
    }

    @Transactional
    @Override
    public void deleteUserImage(String username,String loggedUsername){

        User user = findUserByUsername(username);
        User loggedUser = findUserByUsername(loggedUsername);

        if (isRole_AdminOrSameUser(user, loggedUser)) {
            try {

                Image image = getUserImage(username);

                imageRepository.save(image);

            }catch (MyNotFoundException e){
                throw new IllegalArgumentException("User should have an image to delete it." + e.getLocalizedMessage());
            }
        }else throw new IllegalArgumentException("You are not authorized to delete the image");
    }

    @Transactional
    @Override
    public void deleteCarImage(String username, String loggedUsername)  {

        User user = findUserByUsername(username);
        User loggedUser = findUserByUsername(loggedUsername);

        if (isRole_AdminOrSameUser(user, loggedUser)) {
            try {

                Image image = getCarImage(username);

                imageRepository.save(image);

            }catch (MyNotFoundException e){
                throw new IllegalArgumentException("Car should have an image to delete it." + e.getLocalizedMessage());
            }
        }else throw new IllegalArgumentException("You are not authorized to delete the image");
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new IllegalArgumentException("Username is not recognized"));
    }

    private Car findCarByUser(String username) throws MyNotFoundException {
        return carRepository.findByOwnerAndIsDeletedFalse(username)
                .orElseThrow(() -> new MyNotFoundException("User do not have a car"));
    }

    private boolean isRole_AdminOrSameUser(User user, User loggedUser) {

        return loggedUser.equals(user) || loggedUser.getRole().equals(UserRole.ADMIN);
    }
}
