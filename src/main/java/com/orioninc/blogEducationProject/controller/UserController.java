package com.orioninc.blogEducationProject.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.orioninc.blogEducationProject.exception.PostException;
import com.orioninc.blogEducationProject.exception.UserException;
import com.orioninc.blogEducationProject.model.JsonView.PostView;
import com.orioninc.blogEducationProject.model.JsonView.UserView;
import com.orioninc.blogEducationProject.model.Post;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.service.PostService;
import com.orioninc.blogEducationProject.service.UserService;
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
public class UserController {
    private final UserService userService;
    private final PostService postService;

    @Autowired
    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("users")
    @JsonView(UserView.CommonInfo.class)
    public ResponseEntity users(@RequestParam(required = false) String username,
                                @PageableDefault(size = 30, sort = {"createdDate"},
                                        direction = Sort.Direction.DESC) Pageable pageable){
        Page<User> users;
        if(username == null || username.isEmpty())
            users = userService.getUsers(pageable);
        else
            users = userService.getUsersByUsername(username, pageable);

        return new ResponseEntity(users.getContent(), HttpStatus.OK);
    }

    @GetMapping("users/{username}")
    @JsonView(UserView.Profile.class)
    public ResponseEntity userProfile(@PathVariable String username) throws UserException {
        User userFromDb = userService.getUser(username);

        return new ResponseEntity(userFromDb, HttpStatus.OK);

    }

    @GetMapping("users/{username}/posts")
    @JsonView(PostView.CommonPost.class)
    public ResponseEntity userPosts(@PathVariable String username,
                                    @PageableDefault(size = 15, sort = {"createdDate"},
                                            direction = Sort.Direction.DESC) Pageable pageable) throws PostException {
        Page<Post> userPosts = postService.getPostsByAuthor(username, pageable);

        return new ResponseEntity(userPosts.getContent(), HttpStatus.OK);
    }

    @PutMapping("auth/settings/profile")
    @JsonView(UserView.Profile.class)
    public ResponseEntity editUserProfile(@RequestBody @Valid User editProfile,
                                          @AuthenticationPrincipal User currentUser) throws UserException {
        User changedUser = userService.editProfile(editProfile, currentUser);

        return new ResponseEntity(changedUser, HttpStatus.OK);
    }

    @PutMapping("auth/settings/security")
    @JsonView(UserView.Profile.class)
    public ResponseEntity editUserPassword(@RequestBody @Valid User changeUser,
                                           @AuthenticationPrincipal User currentUser) throws UserException {
        User userFromDb = userService.editPassword(changeUser, currentUser);

        return new ResponseEntity(userFromDb, HttpStatus.OK);
    }
}
