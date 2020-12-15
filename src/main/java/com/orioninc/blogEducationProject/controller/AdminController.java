package com.orioninc.blogEducationProject.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.orioninc.blogEducationProject.exception.PostException;
import com.orioninc.blogEducationProject.exception.UserException;
import com.orioninc.blogEducationProject.model.JsonView.ErrorView;
import com.orioninc.blogEducationProject.model.JsonView.PostView;
import com.orioninc.blogEducationProject.model.JsonView.UserView;
import com.orioninc.blogEducationProject.model.Post;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.service.AdminService;
import com.orioninc.blogEducationProject.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
public class AdminController {
    private final AdminService adminService;
    private final TagService tagService;

    @Autowired
    public AdminController(AdminService adminService, TagService tagService) {
        this.adminService = adminService;
        this.tagService = tagService;
    }

    @GetMapping("users")
    @JsonView(UserView.CommonInfo.class)
    public ResponseEntity userList (@RequestParam(required = false) String username,
                                    @PageableDefault(size = 15, sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable){
        Page<User> users;
        if(username == null || username.isEmpty())
            users = adminService.getUsers(pageable);
        else
            users = adminService.getUsersByUsername(username, pageable);

        return new ResponseEntity(users.getContent(), HttpStatus.OK);
    }

    @GetMapping("users/{username}")
    @JsonView(UserView.Profile.class)
    public ResponseEntity userProfile(@PathVariable("username") String username) throws UserException {
        User userFromDB = adminService.getUser(username);

        return new ResponseEntity(userFromDB, HttpStatus.OK);
    }

    @PutMapping("users/{username}/settings/profile")
    @JsonView(UserView.Profile.class)
    public ResponseEntity editUserProfile(@PathVariable("username") String username,
                                          @RequestBody User editUser) throws UserException {
        editUser = adminService.editUserProfile(username, editUser);

        return new ResponseEntity(editUser, HttpStatus.OK);
    }

    @PutMapping("users/{username}/settings/security")
    @JsonView(UserView.Profile.class)
    public ResponseEntity editUserSecurity(@PathVariable("username") String username,
                                           @RequestBody User editUser) throws UserException {
        editUser = adminService.editUserPassword(username, editUser);

        return new ResponseEntity(editUser, HttpStatus.OK);
    }

    @DeleteMapping("users/{username}")
    @JsonView(UserView.Profile.class)
    public ResponseEntity deleteUser(@PathVariable("username") String username) throws UserException {
        User deleteUser = adminService.deleteUser(username);

        return new ResponseEntity(deleteUser, HttpStatus.OK);
    }

    @GetMapping("posts")
    @JsonView(PostView.CommonPost.class)
    public ResponseEntity postList(@RequestParam(required = false) String topic,
                                   @PageableDefault(size = 15, sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) throws PostException {
        Page<Post> posts;
        if(topic == null)
            posts = adminService.getPosts(pageable);
        else
            posts = adminService.getPostsByTopic(topic, pageable);


        return new ResponseEntity(posts.getContent(), HttpStatus.OK);
    }

    @DeleteMapping("posts/{postId}")
    @JsonView(ErrorView.codeMessage.class)
    public ResponseEntity deletePost(@PathVariable Long postId,
                                     @AuthenticationPrincipal User admin) throws PostException {
        adminService.deletePost(postId, admin);

        return new ResponseEntity(HttpStatus.OK);
    }
}
