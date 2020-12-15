package com.orioninc.blogEducationProject.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.orioninc.blogEducationProject.exception.UserException;
import com.orioninc.blogEducationProject.model.JsonView.UserView;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthorizationController {
    private final UserService userService;

    @Autowired
    public AuthorizationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    @JsonView(UserView.CommonInfo.class)
    public ResponseEntity registrationUser(@RequestBody @Valid User newUser) throws UserException {
        newUser = userService.registrationUser(newUser);

        return new ResponseEntity(newUser, HttpStatus.OK);
    }
}
