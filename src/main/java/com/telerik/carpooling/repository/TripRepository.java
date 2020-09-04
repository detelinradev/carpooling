package com.telerik.carpooling.repository;

import com.telerik.carpooling.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    /**
     *Finds <class>Trip</class> with provided <class>modelId</class> and <class>IsDeleted</class> <class>false</class>
     *
     * @param modelId <class>modelId</class> field for <class>trip</class> to be retrieved from the database
     * @return <class>Optional</class> of <class>Trip</class> instance from database that satisfies the criteria
     */
    Optional<Trip> findByModelIdAndIsDeletedFalse(Long modelId);
}
