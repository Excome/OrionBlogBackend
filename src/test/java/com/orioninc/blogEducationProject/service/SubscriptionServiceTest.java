package com.orioninc.blogEducationProject.service;

import com.orioninc.blogEducationProject.exception.UserException;
import com.orioninc.blogEducationProject.model.Subscription;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.repository.SubscriptionRepository;
import com.orioninc.blogEducationProject.repository.UserRepository;
import com.orioninc.blogEducationProject.service.config.DataConfigTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = DataConfigTest.class, loader = AnnotationConfigContextLoader.class)
@Transactional
@Sql(scripts = "classpath:sql/create-users.sql")
@Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class SubscriptionServiceTest {
/*    @Autowired
    private SubscriptionRepository subRepository;
    @Autowired
    private UserRepository userRepository;
    @Mock
    private UserService userService;

    private SubscriptionService subService;

    @BeforeEach
    void setUp() {
        this.subService = new SubscriptionService(this.subRepository, this.userService);
    }

    @Test
    void subscribe_success() throws UserException {
        User follower = userRepository.findUserById(1l);
        User follow = userRepository.findUserById(2l);
        Subscription sub = new Subscription();
        sub.setFollower(follower);
        sub.setFollow(follow);
        when(userService.getUser("test")).thenReturn(follow);
        assertThat(subService.subscribe(sub, follower))
                .isNotNull()
                .matches(subscription -> !subscription.getId().equals(null))
                .matches(subscription -> subscription.getFollower().getUsername().equals("user"))
                .matches(subscription -> subscription.getFollow().getUsername().equals("test"));

    }

    @Test
    void subscribe_throwUserNotAuthorized() {
        Subscription sub = new Subscription();
        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> subService.subscribe(sub, null))
                .withMessageContaining("User is not authorized");
    }

    @Test
    @Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-sub.sql"})
    @Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void subscribe_throwUserAlreadyIsFollow() throws UserException {
        User follower = userRepository.findUserById(1l);
        User follow = userRepository.findUserById(2l);
        Subscription sub = new Subscription();
        sub.setFollower(follower);
        sub.setFollow(follow);
        when(userService.getUser("test")).thenReturn(follow);
        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> subService.subscribe(sub, follower))
                .withMessageContaining("follower: user, follow: test");
    }

    @Test
    @Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-sub.sql"})
    void unSubscribe_success() throws UserException {
        User follower = userRepository.findUserById(1l);
        User follow = userRepository.findUserById(2l);
        Subscription sub = new Subscription();
        sub.setFollower(follower);
        sub.setFollow(follow);

        when(userService.getUser(follow.getUsername())).thenReturn(follow);

        subService.unSubscribe(sub, follower);
        assertThat(subRepository.findByFollowerAndFollow(follower, follow))
                .isNull();
    }

    @Test
    void unSubscribe_throwUserUnauthorized(){
        User follower = userRepository.findUserById(1l);
        User follow = userRepository.findUserById(2l);
        Subscription sub = new Subscription();
        sub.setFollower(follower);
        sub.setFollow(follow);

        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> subService.unSubscribe(sub, null))
                .withMessageContaining("User is not authorized");

        User anotherUser = new User();
        anotherUser.setUsername("test2");
        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> subService.unSubscribe(sub, anotherUser))
                .withMessageContaining("User is not authorized");
    }

    @Test
    @Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-sub.sql"})
    @Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void unSubscribe_throwUserNotFollow() throws UserException {
        User follower = userRepository.findUserById(1l);
        User follow = userRepository.findUserById(2l);
        Subscription sub = new Subscription();
        sub.setFollower(follower);
        sub.setFollow(follow);

        when(userService.getUser(follow.getUsername())).thenReturn(follow);
        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> subService.unSubscribe(sub, follower))
                .withMessageContaining("follower: user, follow: test");
    }

    @Test
    @Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-sub.sql"})
    @Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUserFollowers_success() throws UserException {
        User test = userRepository.findUserById(2l);
        Pageable pageable = PageRequest.of(0, 20);
        when(userService.getUser("test")).thenReturn(test);
        List<User> userFollowers = subService.getUserFollowers(test.getUsername(), pageable);

        assertThat(userFollowers).isNotEmpty().hasSize(1);
    }

    @Test
    @Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-sub.sql"})
    @Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getUserFollow_success() throws UserException {
        User user = userRepository.findUserById(1l);
        Pageable pageable = PageRequest.of(0, 20);
        when(userService.getUser("user")).thenReturn(user);
        List<User> userFollow = subService.getUserFollow(user.getUsername(), pageable);

        assertThat(userFollow).isNotEmpty().hasSize(1);
    }*/
}