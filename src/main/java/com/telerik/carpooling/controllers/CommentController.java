package com.telerik.carpooling.controllers;

import com.telerik.carpooling.models.dtos.CommentDtoEdit;
import com.telerik.carpooling.models.dtos.CommentDtoResponse;
import com.telerik.carpooling.services.services.contracts.CommentService;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping(value = "/{tripId}")
    public ResponseEntity<CommentDtoResponse> createComment(@PathVariable final Long tripId,
                                                            final Authentication authentication,
                                                            @RequestParam(value = "comment") final String message)
            throws NotFoundException {

        return ResponseEntity.ok().body(commentService.createComment(tripId, authentication.getName(), message));
    }

    @GetMapping(value = "/{tripId}")
    public ResponseEntity<Set<CommentDtoResponse>> getComments(@PathVariable final Long tripId)
            throws NotFoundException {

        return ResponseEntity.ok().body(commentService.getComments(tripId));
    }

    @PutMapping
    public ResponseEntity<CommentDtoResponse> editComment(@Valid @RequestBody final CommentDtoEdit commentDtoEdit,
                                                       final Authentication authentication) {

        return ResponseEntity.ok().body(commentService.updateComment(commentDtoEdit, authentication.getName()));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable final Long id, final Authentication authentication)
            throws NotFoundException {

        commentService.deleteComment(id,authentication.getName());
        return ResponseEntity.ok().build();
    }
}
