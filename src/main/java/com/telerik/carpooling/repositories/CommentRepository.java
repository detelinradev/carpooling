package com.telerik.carpooling.repositories;

import com.telerik.carpooling.models.Comment;
import com.telerik.carpooling.models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Set<Comment> getAllByTripAndIsDeletedFalse(Trip trip);
}
