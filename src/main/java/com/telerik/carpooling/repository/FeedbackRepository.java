package com.telerik.carpooling.repository;

import com.telerik.carpooling.model.Feedback;
import com.telerik.carpooling.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

    Set<Feedback> getAllByUserAndIsDeletedFalse(User value);
}
