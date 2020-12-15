package com.orioninc.blogEducationProject.service;

import com.orioninc.blogEducationProject.exception.UserException;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.repository.RoleRepository;
import com.orioninc.blogEducationProject.repository.UserRepository;
import com.orioninc.blogEducationProject.service.config.DataConfigTest;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = DataConfigTest.class, loader = AnnotationConfigContextLoader.class)
@Transactional
@Sql(scripts = {"classpath:sql/clear-tables.sql", "classpath:sql/create-users.sql"})
@Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;


    private UserService userService;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(this.userRepository, this.roleRepository, this.passwordEncoder);
    }

    @Test
    void registration_success() throws UserException {
        User newUser = new User();
        newUser.setEmail("reg@test.com");
        newUser.setUsername("register");
        newUser.setPassword("test");
        newUser.setPasswordConfirm("test");
        when(passwordEncoder.encode("test")).thenReturn("testHash");
        User savedUser = newUser;
        assertThat(userService.registrationUser(newUser))
                .matches(user -> user.getId() != null)
                .hasFieldOrPropertyWithValue("username", "register")
                .hasFieldOrPropertyWithValue("email", "reg@test.com")
                .hasFieldOrPropertyWithValue("password", "testHash")
                .hasFieldOrPropertyWithValue("surname", "")
                .hasFieldOrPropertyWithValue("name", "");
    }

    @Test
    void registrationUser_throwEmailIsAlreadyUsed() {
        User newUser = new User();
        newUser.setEmail("user@user.com");

        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> userService.registrationUser(newUser)).withMessageContaining("email: user@user.com");
    }

    @Test
    void registrationUser_throwUsernameIsAlreadyUsed() throws UserException {
        User newUser = new User();
        newUser.setEmail("test@test.com");
        newUser.setUsername("user");

        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> userService.registrationUser(newUser)).withMessageContaining("username: user");
    }

    @Test
    void registrationUser_PasswordsAreNotEqual() throws UserException {
        User newUser = new User();
        newUser.setPassword("123");
        newUser.setPasswordConfirm("12345");
        newUser.setEmail("test@test.com");
        newUser.setUsername("user2");

        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> userService.registrationUser(newUser)).withMessageContaining("Password and password confirm do not match");
    }

    @Test
    void getUser_returnUser() throws UserException {
        assertThat(userService.getUser("user"))
                .isNotNull()
                .matches(user -> user.getId() == 1L)
                .hasFieldOrPropertyWithValue("username", "user")
                .hasFieldOrPropertyWithValue("email", "user@user.com");
    }

    @Test
    void getUser_throwUserNotFound() throws UserException {
        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> userService.getUser("test2")).withMessageContaining("user: test2");
    }

    @Test
    void editProfile_success() throws ParseException, UserException {
        User userFromDB = userRepository.findUserByUsername("test");
        User editProfile = new User();
        editProfile.setUsername("test");
        editProfile.setSurname("testSurname");
        editProfile.setName("testName");
        editProfile.setBirthday(DateUtils.parseDate("02.06.2000", "dd.MM.yyyy"));
        editProfile.setPrivateStatus(true);

        assertThat(userService.editProfile(editProfile, userFromDB))
                .hasFieldOrPropertyWithValue("surname", "testSurname")
                .hasFieldOrPropertyWithValue("name", "testName")
                .hasFieldOrPropertyWithValue("birthday", DateUtils.parseDate("02.06.2000", "dd.MM.yyyy"))
                .hasFieldOrPropertyWithValue("privateStatus", true);
    }

    @Test
    void editProfile_throwUserNotAuthorized() throws UserException {
        User userFromDb = userRepository.findUserByUsername("test");
        User editProfile = new User();
        editProfile.setUsername("test2");

        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> userService.editProfile(editProfile, userFromDb))
                .withMessageContaining("User is not authorized");
    }

    @Test
    void editPassword_success() throws UserException {
        User userFromDB = userRepository.findUserByUsername("test");

        User editProfile = new User();
        editProfile.setUsername("test");
        editProfile.setPassword("123456");
        editProfile.setPasswordConfirm("123456");

        when(passwordEncoder.encode("123456")).thenReturn("hashPassword");
        assertThat(userService.editPassword(editProfile, userFromDB))
                .isEqualTo(userFromDB)
                .hasFieldOrPropertyWithValue("password", "hashPassword");
    }

    @Test
    void editPassword_throwUserNotAuthorize(){
        User userFromDB = userRepository.findUserByUsername("test");

        User editProfile = new User();
        editProfile.setUsername("test2");

        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> userService.editPassword(editProfile, userFromDB))
                .withMessageContaining("User is not authorized");
    }

    @Test
    void editPassword_throwPasswordAreNotEqual(){
        User userFromDB = userRepository.findUserByUsername("test");


        User editProfile = new User();
        editProfile.setUsername("test");
        editProfile.setPassword("123456");
        editProfile.setPasswordConfirm("123");

        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> userService.editPassword(editProfile, userFromDB))
                .withMessageContaining("Password and password confirm do not match");
    }

    @Test
    void getUsers_success(){
        Pageable pageable = PageRequest.of(0, 15);
        List<User> users = userService.getUsers(pageable).getContent();
        assertThat(users)
                .isNotEmpty()
                .hasSize(2)
                .allMatch(user -> user.getId() != null);
    }

    @Test
    void getUsersByUsername_success(){
        Pageable pageable = PageRequest.of(0, 15);
        List<User> users = userService.getUsersByUsername("e", pageable).getContent();
        assertThat(users)
                .isNotEmpty()
                .hasSize(2)
                .allMatch(user -> user.getId() != null)
                .allMatch(user -> user.getUsername().contains("e"));
    }

}
