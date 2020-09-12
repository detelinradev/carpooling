package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.Image;
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

    /**
     *      Fetches from database <class>Image</class> object for the passed <class>User</class>.
     *  <p>
     *      Check is made if passed <class>username</class>is valid, otherwise exception is thrown.
     *
     * @param username the <class>username</class> of this<class>user</class> which <class>image</class> we are fetching
     * @return retrieved from database <class>Image</class> object
     * @throws MyNotFoundException for user to not have image is expected and recoverable condition so checked
     * exception is thrown which is then propagated to controller and global exception handler
     */
    Image getUserImage(String username) throws MyNotFoundException;

    /**
     *      Fetches from database <class>Image</class> object for the passed <class>Car</class>.
     *  <p>
     *      Check is made if passed <class>username</class>is valid and passed <class>user</class> has a
     *      <class>car</class> , otherwise exception is thrown.
     *
     * @param username the <class>username</class> of this<class>user</class> which <class>car</class>
     *                 <class>image</class> we are fetching
     * @return retrieved from database <class>Image</class> object
     * @throws MyNotFoundException for car to not have image is expected and recoverable condition so checked
     * exception is thrown which is then propagated to controller and global exception handler
     */
    Image getCarImage(String username) throws MyNotFoundException;

    /**
     *     Deleting <class>user</class> <class>image</class> by given <class>username</class>.
     * <p>
     *     Validation is made for the passed <class>user</class> <class>username</class> fields, if they are not matching
     * <class>user</class> exception is thrown, then is checked if the user deleting has authority to do that, otherwise
     * exception is thrown.
     * <p>
     *     Checked exception is caught if <class>user</class> do not have <class>image</class> and unchecked is thrown as
     * it is not expected to be deleted if image does not exist.
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param username the <class>username</class> of this<class>user</class> <class>image</class> we are deleting
     * @param loggedUsername <class>username</class> of the currently logged <class>user</class> extracted from
     *                       the security context thread
     */
    void deleteUserImage(String username, String loggedUsername);

    /**
     *     Deleting <class>car</class> <class>image</class> by given <class>user</class> <class>username</class>.
     * <p>
     *     Validation is made for the passed <class>user</class> <class>username</class> fields, if they are not matching
     * <class>user</class> exception is thrown, then is checked if the user deleting has authority to do that, otherwise
     * exception is thrown.
     * <p>
     *     Checked exception is caught if <class>car</class> do not have <class>image</class> and unchecked is thrown as
     * it is not expected to be deleted if image does not exist.
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param username the <class>username</class> of this<class>user</class> which <class>car</class>
     *      *                 <class>image</class> we are deleting
     * @param loggedUsername <class>username</class> of the currently logged <class>user</class> extracted from
     *                       the security context thread
     */
    void deleteCarImage(String username, String loggedUsername) ;

}
