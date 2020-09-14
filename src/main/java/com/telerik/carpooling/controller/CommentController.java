package com.telerik.carpooling.controller;

import com.telerik.carpooling.model.dto.CommentDtoEdit;
import com.telerik.carpooling.model.dto.CommentDtoResponse;
import com.telerik.carpooling.service.service.contract.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

@CrossOrigin(maxAge = 3600)
@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("comments")
@Validated
public class CommentController {

    private final CommentService commentService;

    @PostMapping(value = "/{tripId}")
    public ResponseEntity<CommentDtoResponse> createComment(@PathVariable @NotNull final Long tripId,
                                                            @RequestParam(value = "comment")
                                                            @NotNull final String message) {

        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok().body(commentService.createComment(tripId, loggedUserName, message));
    }

    @GetMapping(value = "/{tripId}")
    public ResponseEntity<Set<CommentDtoResponse>> getComments(@PathVariable @NotNull final Long tripId) {

        return ResponseEntity.ok().body(commentService.getComments(tripId));
    }

    @PutMapping
    public ResponseEntity<CommentDtoResponse> editComment(@Valid @RequestBody final CommentDtoEdit commentDtoEdit) {

        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        return ResponseEntity.ok().body(commentService.updateComment(commentDtoEdit, loggedUserName));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable @NotNull final Long id) {

        String loggedUserName = SecurityContextHolder.getContext().getAuthentication().getName();

        commentService.deleteComment(id, loggedUserName);
        return ResponseEntity.ok().build();
    }
}
