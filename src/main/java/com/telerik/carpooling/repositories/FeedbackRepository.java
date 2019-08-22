package com.telerik.carpooling.repositories;

import com.telerik.carpooling.models.Feedback;
import com.telerik.carpooling.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

    Set<Feedback> getAllByUser(User value);
}
