package com.telerik.carpooling.controller;

import com.telerik.carpooling.model.dto.CommentDtoEdit;
import com.telerik.carpooling.model.dto.CommentDtoResponse;
import com.telerik.carpooling.service.service.contract.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
                                                            final Authentication authentication,
                                                            @RequestParam(value = "comment") @NotNull final String message) {

        return ResponseEntity.ok().body(commentService.createComment(tripId, authentication.getName(), message));
    }

    @GetMapping(value = "/{tripId}")
    public ResponseEntity<Set<CommentDtoResponse>> getComments(@PathVariable @NotNull final Long tripId){

        return ResponseEntity.ok().body(commentService.getComments(tripId));
    }

    @PutMapping
    public ResponseEntity<CommentDtoResponse> editComment(@Valid @RequestBody final CommentDtoEdit commentDtoEdit,
                                                       final Authentication authentication) {

        return ResponseEntity.ok().body(commentService.updateComment(commentDtoEdit, authentication.getName()));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable @NotNull final Long id, final Authentication authentication){

        commentService.deleteComment(id,authentication.getName());
        return ResponseEntity.ok().build();
    }
}
