package com.telerik.carpooling.repositories;

import com.telerik.carpooling.models.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {
}
