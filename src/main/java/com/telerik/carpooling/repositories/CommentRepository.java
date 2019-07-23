package com.telerik.carpooling.repositories;

import com.telerik.carpooling.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
