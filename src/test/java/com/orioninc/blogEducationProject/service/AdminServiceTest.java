package com.orioninc.blogEducationProject.service;

import com.orioninc.blogEducationProject.exception.UserException;
import com.orioninc.blogEducationProject.model.Role;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.repository.PostRepository;
import com.orioninc.blogEducationProject.repository.RoleRepository;
import com.orioninc.blogEducationProject.repository.SubscriptionRepository;
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
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = DataConfigTest.class, loader = AnnotationConfigContextLoader.class)
@Transactional
@Sql(scripts = "classpath:sql/clear-tables.sql")
@Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-sub.sql"})
class AdminServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SubscriptionRepository subRepository;
    @Autowired
    private PostRepository postRepository;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;
    @Mock
    private PostService postService;


    private AdminService adminService;


    @BeforeEach
    void setUp() {
        this.adminService = new AdminService(userRepository, roleRepository, passwordEncoder, postService, subRepository);
    }

    @Test
    void deleteUser_success() throws UserException {
        User user = userRepository.findUserByUsername("user");
        adminService.deleteUser("user");
        Pageable pageable = PageRequest.of(0, 20);
        assertThat(subRepository.findAllByFollower(user, pageable)).isEmpty();
        assertThat(subRepository.findAllByFollow(user, pageable)).isEmpty();
        assertThat(userRepository.findUserByUsername("user")).isNull();

    }

    @Test
    void deleteUser_throwUserNotFound(){
        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> adminService.deleteUser("userTwo"))
                .withMessageContaining("not found | username: userTwo");

    }

    @Test
    @Sql(scripts = "classpath:sql/clear-tables.sql")
    @Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-posts.sql"})
    void getPosts_success() {
        Pageable pageable = PageRequest.of(0, 15);
        when(postService.getPosts(pageable)).thenReturn(postRepository.findAll(pageable));
        assertThat(adminService.getPosts(pageable).getContent())
                .isNotEmpty()
                .hasSize(2)
                .allMatch(post -> post.getId() != null);
    }

    @Test
    void editUserProfile_success() throws ParseException, UserException {
        User test = new User();
        test.setUsername("test2");
        test.setEmail("test@test.ru");
        test.setSurname("testSur");
        test.setName("testName");
        test.setPrivateStatus(true);
        test.setBirthday((DateUtils.parseDate("11.11.2011", "dd.MM.yyyy")));

        Set<Role> newRoles = new HashSet<>();
        newRoles.add(new Role(1L, "ROLE_USER"));
        newRoles.add(new Role(2L, "ROLE_ADMIN"));
        test.setRoles(newRoles);
        User testAfter = adminService.editUserProfile("test", test);
        assertThat(testAfter)
                .matches(user -> user.getId() != null)
                .hasFieldOrPropertyWithValue("email", test.getEmail())
                .hasFieldOrPropertyWithValue("username", test.getUsername())
                .hasFieldOrPropertyWithValue("surname", test.getSurname())
                .hasFieldOrPropertyWithValue("name", test.getName())
                .hasFieldOrPropertyWithValue("privateStatus", test.isPrivateStatus())
                .hasFieldOrPropertyWithValue("birthday", test.getBirthday())
                .hasFieldOrPropertyWithValue("roles", test.getRoles());
    }

    @Test
    void editUserProfile_throwEmailIsAlreadyUsed(){
        User test = new User();
        test.setUsername("test");
        test.setEmail("user@user.com");
        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> adminService.editUserProfile("test", test))
                .withMessageContaining("email: user@user.com");
    }

    @Test
    void editUserProfile_throwUsernameIsAlreadyUsed(){
        User test = new User();
        test.setUsername("user");
        test.setEmail("test@test.ru");
        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> adminService.editUserProfile("test", test))
                .withMessageContaining("username: user");
    }

    @Test
    void editUserPassword_success() throws UserException {
        User test = new User();
        test.setPassword("123456");
        test.setPasswordConfirm("123456");
        when(passwordEncoder.encode(test.getPassword())).thenReturn("123456hash");
        User userAfter = adminService.editUserPassword("test", test);
        assertThat(userAfter)
                .matches(user -> user.getId() != null)
                .hasFieldOrPropertyWithValue("password", "123456hash")
                .hasFieldOrPropertyWithValue("passwordConfirm", null);
    }

    @Test
    void editUserPassword_throwPasswordsAreNotEqual() {
        User test = new User();
        test.setPassword("123456");
        test.setPasswordConfirm("12345");
        assertThatExceptionOfType(UserException.class)
                .isThrownBy(() -> adminService.editUserPassword("test", test))
                .withMessageContaining("Password and password confirm do not match");
    }
}