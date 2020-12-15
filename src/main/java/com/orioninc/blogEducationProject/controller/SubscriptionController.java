package com.orioninc.blogEducationProject.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.orioninc.blogEducationProject.exception.UserException;
import com.orioninc.blogEducationProject.model.JsonView.ErrorView;
import com.orioninc.blogEducationProject.model.JsonView.SubscriptionView;
import com.orioninc.blogEducationProject.model.Subscription;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class SubscriptionController {
    private final SubscriptionService subService;

    @Autowired
    public SubscriptionController(SubscriptionService subService) {
        this.subService = subService;
    }

    @GetMapping("{username}/subscription/followers")
    @JsonView(SubscriptionView.FollowerFollow.class)
    public ResponseEntity userFollowers(@PathVariable String username,
                                        @PageableDefault(size = 100, sort = {"createdDate"},
                                                direction = Sort.Direction.DESC) Pageable pageable) throws UserException {
        List<User> userFollowers = subService.getUserFollowers(username, pageable);

        return new ResponseEntity(userFollowers, HttpStatus.OK);
    }

    @GetMapping("{username}/subscription/follow")
    @JsonView(SubscriptionView.FollowerFollow.class)
    public ResponseEntity userFollow(@PathVariable String username,
                                     @PageableDefault(size = 100, sort = {"createdDate"},
                                             direction = Sort.Direction.DESC) Pageable pageable) throws UserException {
        List<User> userFollow = subService.getUserFollow(username, pageable);

        return new ResponseEntity(userFollow, HttpStatus.OK);
    }

    @PostMapping("{username}/follow")
    @JsonView(SubscriptionView.FollowerFollow.class)
    public ResponseEntity subscribe(@RequestBody Subscription sub,
                                    @AuthenticationPrincipal User currentUser) throws UserException {
        sub = subService.subscribe(sub, currentUser);

        return new ResponseEntity(sub, HttpStatus.OK);
    }

    @DeleteMapping("{username}/follow")
    @JsonView(ErrorView.codeMessage.class)
    public ResponseEntity unSubscribe(@RequestBody Subscription sub,
                                      @AuthenticationPrincipal User currentUser) throws UserException {
        subService.unSubscribe(sub, currentUser);

        return new ResponseEntity(HttpStatus.OK);
    }
}
