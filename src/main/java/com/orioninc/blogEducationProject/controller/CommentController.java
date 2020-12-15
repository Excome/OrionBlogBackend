package com.orioninc.blogEducationProject.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.orioninc.blogEducationProject.exception.CommentException;
import com.orioninc.blogEducationProject.model.Comment;
import com.orioninc.blogEducationProject.model.JsonView.CommentView;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("posts")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("{postId}/comments")
    @JsonView(CommentView.FullCommentWithOutPost.class)
    public ResponseEntity postComments(@PathVariable Long postId,
                                       @PageableDefault(size = 20, sort = {"createdDate"},
                                               direction = Sort.Direction.DESC) Pageable pageable) throws CommentException {
        Page<Comment> comments = commentService.getPostComments(postId, pageable);

        return new ResponseEntity(comments.getContent(), HttpStatus.OK);

    }

    @PostMapping("{postId}/comments")
    @JsonView(CommentView.FullCommentWithOutPost.class)
    public ResponseEntity createComment(@RequestBody @Valid Comment newComment,
                                        @PathVariable Long postId,
                                        @AuthenticationPrincipal User currentUser) throws CommentException {
        newComment = commentService.createComment(newComment, currentUser, postId);

        return new ResponseEntity(newComment, HttpStatus.OK);

    }

    @PutMapping("{postId}/comments/{commentId}")
    @JsonView(CommentView.FullCommentWithOutPost.class)
    public ResponseEntity editComment(@PathVariable Long postId,
                                      @PathVariable Long commentId,
                                      @RequestBody @Valid Comment changedComment,
                                      @AuthenticationPrincipal User currentUser) throws CommentException {
        changedComment = commentService.editComment(commentId,  postId, changedComment,  currentUser);

        return new ResponseEntity(changedComment, HttpStatus.OK);

    }

    @DeleteMapping("{postId}/comments/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long commentId,
                                        @AuthenticationPrincipal User currentUser) throws CommentException {
        commentService.deleteComment(commentId, currentUser);

        return new ResponseEntity(HttpStatus.OK);
    }
}
