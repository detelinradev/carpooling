package com.telerik.carpooling.services.services.contracts;

import com.telerik.carpooling.models.Image;
import com.telerik.carpooling.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

public interface ImageService {

    Image storeCarImage(MultipartFile file, User user, URI fileDownloadUri);

    Image storeUserImage(MultipartFile file, User user, URI fileDownloadUri);

    Image getImage(long fileId);
}
