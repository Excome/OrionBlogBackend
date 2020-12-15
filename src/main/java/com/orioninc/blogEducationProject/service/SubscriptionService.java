package com.orioninc.blogEducationProject.service;

import com.orioninc.blogEducationProject.error.UserError;
import com.orioninc.blogEducationProject.exception.UserException;
import com.orioninc.blogEducationProject.model.Subscription;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subRepository;
    private final UserService userService;

    @Autowired
    public SubscriptionService(SubscriptionRepository subRepository, UserService userService) {
        this.subRepository = subRepository;
        this.userService = userService;
    }

    public Subscription subscribe(Subscription sub, User follower) throws UserException {
        if(follower == null || !sub.getFollower().getUsername().equals(follower.getUsername()))
            throw new UserException(UserError.USER_NOT_AUTHORIZED);

        User follow = userService.getUser(sub.getFollow().getUsername());

        if(subRepository.findByFollowerAndFollow(follower, follow) != null)
            throw new UserException(UserError.USER_ALREADY_IS_FOLLOW, "follower: " + follower.getUsername() + ", follow: " + follow.getUsername());

        sub.setFollower(follower);
        sub.setFollow(follow);

        return subRepository.save(sub);
    }

    public void unSubscribe(Subscription sub, User follower) throws UserException {
        if(follower == null || !sub.getFollower().getUsername().equals(follower.getUsername()))
            throw new UserException(UserError.USER_NOT_AUTHORIZED);

        User follow = userService.getUser(sub.getFollow().getUsername());
        sub = subRepository.findByFollowerAndFollow(follower, follow);

        if(sub == null)
            throw new UserException(UserError.USER_NOT_FOLLOW, "follower: " + follower.getUsername() + ", follow: " + follow.getUsername());

        subRepository.delete(sub);
    }


    public List<User> getUserFollowers(String username, Pageable pageable) throws UserException {
        User userFromDB = userService.getUser(username);

        return subRepository.findAllByFollow(userFromDB, pageable).getContent().stream()
                .map(f -> f.getFollower())
                .collect(Collectors.toList());
    }

    public List<User> getUserFollow(String username, Pageable pageable) throws UserException {
        User userFromDB = userService.getUser(username);

        return subRepository.findAllByFollower(userFromDB, pageable).getContent().stream()
                .map(f -> f.getFollow())
                .collect(Collectors.toList());
    }
}
