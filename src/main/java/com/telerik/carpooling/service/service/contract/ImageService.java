package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Image;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    /**
     *     Stores <class>image</class> of the <class>car</class> if <class>car</class> is present.
     * <p>
     *     There is threshold of 2 KB for writing to disk, limit 1 MB for upload and 1 MB for download.
     * <p>
     *     checked <class>IOException</class> is caught and rethrown as unchecked <class>FileStorageException</class>
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param file multipart file holding data for the <class><image/class>
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from
     *                           the security context thread
     */
    void storeCarImage(MultipartFile file, String loggedUserUsername);

    /**
     *     Stores <class>image</class> of the user.
     * <p>
     *     There is threshold of 2 KB for writing to disk, limit 1 MB for upload and 1 MB for download.
     * <p>
     *     checked <class>IOException</class> is caught and rethrown as unchecked <class>FileStorageException</class>
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param file multipart file holding data for the image
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from
     *                           the security context thread
     */
    void storeUserImage(MultipartFile file, String loggedUserUsername);

    Image getUserImage(String username) throws MyNotFoundException;

    Image getCarImage(String username) throws MyNotFoundException;

    void deleteUserImage(String username, Authentication authentication) throws MyNotFoundException;

    void deleteCarImage(String username, Authentication authentication) throws MyNotFoundException;

}
