package com.telerik.carpooling.services;

import com.telerik.carpooling.exceptions.FileStorageException;
import com.telerik.carpooling.exceptions.MyFileNotFoundException;
import com.telerik.carpooling.models.Image;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.repositories.ImageRepository;
import com.telerik.carpooling.repositories.UserRepository;
import com.telerik.carpooling.services.services.contracts.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

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
            imageRepository.save(image);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public void storeCarImage( MultipartFile file,String loggedUserUsername) {

        User user = findUserByUsername(loggedUserUsername);
        if(user.getCar() != null) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                if (fileName.contains("..")) {
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                }
                Image image = new Image(file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes(), user.getCar());
                imageRepository.save(image);
            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
            }
        }
    }

    @Override
    public Image getUserImage(String username) {
        User user = findUserByUsername(username);
        Long fileId = user.getUserImage().getModelId();

        return imageRepository.findByModelId(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with imageId " + fileId));
    }

    @Override
    public Image getCarImage(String username) {
        User user = findUserByUsername(username);
        Long fileId = user.getCar().getCarImage().getModelId();

        return imageRepository.findByModelId(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with imageId " + fileId));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsernameAndIsDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username is not recognized"));
    }
}
