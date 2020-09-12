package com.telerik.carpooling.service.service.contract;

import com.telerik.carpooling.exception.MyNotFoundException;
import com.telerik.carpooling.model.dto.CarDtoEdit;
import com.telerik.carpooling.model.dto.CarDtoRequest;
import com.telerik.carpooling.model.dto.CarDtoResponse;

public interface CarService {

    /**
     *     Creates <class>Car</class> object from passed parameters.
     * <p>
     *     It is expected the DTO to be valid data, based on a validity check in the controller with annotation @Valid
     * and restrains upon the creation of the DTO object. However it that is not the case, validation criteria are as well
     * placed in the actual entity class, what would fire an exception if invalid data is provided. Parameter
     * loggedUserUsername is extracted from security context and therefore is considered safe.
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param carDtoRequest DTO that holds required data for creating <class>Car</class> object
     * @param loggedUserUsername loggedUserUsername <class>username</class> of the currently logged <class>user</class>
     *                           extracted from the security context thread
     * @return instance of the created <class>Car</class> mapped as <class>CarDtoResponse</class>
     */
    CarDtoResponse createCar(CarDtoRequest carDtoRequest, String loggedUserUsername);

    /**
     *     Updates <class>car</class> from given DTO object.
     * <p>
     *     There is check if the logged <class>user</class> is the owner of this <class>car</class>, if
     * the criteria is met, this <class>car</class> is updated, otherwise exception is thrown as that is not the
     * expected behavior.
     * <p>
     *     It is expected the input to be valid data, based on a validity check in the controller with annotation @Valid
     * and restrains upon the creation of the DTO object. However it that is not the case, validation criteria are as well
     * placed in the actual entity class, what would fire an exception if invalid data is provided. Parameter
     * loggedUserUsername is extracted from security context and therefore is considered safe.
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *
     * @param carDtoEdit DTO that holds required data for updating <class>Car</class> object
     * @param loggedUserUsername loggedUserUsername <class>username</class> of the currently logged <class>user</class>
     *                           extracted from the security context thread
     * @return instance of the updated <class>car</class> object mapped as <class>CarDtoResponse</class>
     */
    CarDtoResponse updateCar(CarDtoEdit carDtoEdit, String loggedUserUsername);

    /**
     *   Deletes softly <class>car</class> with given <class>car</class> <class>modelId</class>
     * <p>
     *     There is check if the logged <class>user</class> is the owner of this <class>car</class> or
     * <class>admin</class>, if the criteria is met, the <class>car</class> is softly delete, otherwise
     * IllegalArgumentException exception is thrown as that is not the expected result.
     * <p>
     *     Validation is made for <class>car</class> <class>modelId</class>, if it is not passed exception is thrown.
     * Parameter loggedUserUsername is trusted and is not checked as it is extracted from the security context as a
     * currently logged <class>user</class>.
     * <p>
     *     Transactional annotation is added to override class based behavior read only = true, with read only = false, as
     * this method is modifying the entity so we expect Hibernate to observe changes in the current Persistence Context
     * and include update at flush-time.
     *  @param carId the <class>modelId</class> of the <class>comment</class> to be deleted
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from
     */
    void deleteCar(Long carId, String loggedUserUsername) ;

    /**
     *      Search for <class>car</class> object by parameter <class>user</class>.
     *  <p>
     *      Parameter loggedUserUsername is extracted from security context
     *  and therefore is considered safe.
     *  <p>
     *      It is expected behaviour user to not have car, therefore checked exception is thrown in that case.
     *
     * @param loggedUserUsername <class>username</class> of the currently logged <class>user</class> extracted from
     *                           the security context thread
     * @return instance of the fetched <class>Car</class> mapped as <class>CarDtoResponse</class>
     * @throws MyNotFoundException throws checked exception if the <class>user</class> does not have <class>car</class>
     */
    CarDtoResponse getCar(String loggedUserUsername) throws MyNotFoundException;
}
