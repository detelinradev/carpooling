package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.model.Image;
import javassist.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void storeCarImage(MultipartFile file, String loggedUserUsername);

    void storeUserImage(MultipartFile file, String loggedUserUsername);

    Image getUserImage(String username);

    Image getCarImage(String username) throws NotFoundException;

    void deleteUserImage(String username, Authentication authentication);

    void deleteCarImage(String username, Authentication authentication) throws NotFoundException;

}
