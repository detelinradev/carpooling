package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.Image;
import com.telerik.carpooling.models.User;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void storeCarImage(MultipartFile file, User user);

    void storeUserImage(MultipartFile file, User user);

    Image getImage(int fileId);
}
