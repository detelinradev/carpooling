package com.telerik.carpooling.repository;

import com.telerik.carpooling.model.Comment;
import com.telerik.carpooling.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Set<Comment> getAllByTripAndIsDeletedFalse(Trip trip);
}
