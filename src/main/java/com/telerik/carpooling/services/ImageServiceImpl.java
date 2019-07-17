package com.telerik.carpooling.services;

import com.telerik.carpooling.exceptions.FileStorageException;
import com.telerik.carpooling.exceptions.MyFileNotFoundException;
import com.telerik.carpooling.models.Image;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.repositories.ImageRepository;
import com.telerik.carpooling.services.services.contracts.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    public void storeUserImage(final MultipartFile file,final User user) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Image image = new Image(file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes(),user);

            imageRepository.save(image);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public void storeCarImage(final MultipartFile file,final User user) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }
            Image image = new Image(file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes(),user.getCar());

            imageRepository.save(image);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Image getImage(final int fileId) {
        return imageRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with imageId " + fileId));
    }
}
