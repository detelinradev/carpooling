package com.telerik.carpooling.services;

import com.telerik.carpooling.models.Car;
import com.telerik.carpooling.models.Image;
import com.telerik.carpooling.models.User;
import com.telerik.carpooling.models.dtos.CarDto;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void storeCarImage(MultipartFile file, User user);

    void storeUserImage(MultipartFile file, User user);

    Image getImage(int fileId);
}
