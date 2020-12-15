package com.orioninc.blogEducationProject.service;

import com.orioninc.blogEducationProject.error.UserError;
import com.orioninc.blogEducationProject.exception.PostException;
import com.orioninc.blogEducationProject.exception.UserException;
import com.orioninc.blogEducationProject.model.Post;
import com.orioninc.blogEducationProject.model.Role;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.repository.CommentRepository;
import com.orioninc.blogEducationProject.repository.RoleRepository;
import com.orioninc.blogEducationProject.repository.SubscriptionRepository;
import com.orioninc.blogEducationProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminService extends UserService{

    private final PostService postService;
    private final SubscriptionRepository subRepository;

    @Autowired
    public AdminService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, PostService postService, SubscriptionRepository subRepository) {
        super(userRepository, roleRepository, bCryptPasswordEncoder);
        this.postService = postService;
        this.subRepository = subRepository;
    }

    @Transactional
    public User deleteUser(String username) throws UserException {
        User userFromDb = userRepository.findUserByUsername(username);

        if(userFromDb == null)
            throw new UserException(UserError.USER_NOT_FOUND, "username: " + username);

        subRepository.deleteAllByFollowerOrFollow(userFromDb, userFromDb);
        postService.deleteAllUserPosts(userFromDb);
        userRepository.delete(userFromDb);

        return userFromDb;
    }

    public Page<Post> getPosts(Pageable pageable){
        return postService.getPosts(pageable);
    }

    public Page<Post> getPostsByTopic(String topic, Pageable pageable) throws PostException {
        return postService.getPostsByTopic(topic, pageable);
    }

    public User editUserProfile(String username, User editUser) throws UserException {
        User userFromDb = getUser(username);

        if(!userFromDb.getEmail().equals(editUser.getEmail())) {
            if (userRepository.findUserByEmail(editUser.getEmail()) != null)
                throw new UserException(UserError.EMAIL_IS_ALREADY_USED, "email: " + editUser.getEmail());
            userFromDb.setEmail(editUser.getEmail());
        }

        if(!userFromDb.getUsername().equals(editUser.getUsername())) {
            if (userRepository.findUserByUsername(editUser.getUsername()) != null)
                throw new UserException(UserError.USERNAME_IS_ALREADY_USED, "username: " + editUser.getUsername());
            userFromDb.setUsername(editUser.getUsername());
        }
        userFromDb.setName(editUser.getName());
        userFromDb.setSurname(editUser.getSurname());
        userFromDb.setPrivateStatus(editUser.isPrivateStatus());
        userFromDb.setBirthday(editUser.getBirthday());

        Set<Role> roles = editUser.getRoles().stream()
                .map(role -> role = roleRepository.findRoleByNameContains(role.getName()))
                .collect(Collectors.toSet());
        userFromDb.setRoles(roles);

        return userRepository.save(userFromDb);
    }

    public User editUserPassword(String username, User editUser) throws UserException {
        User userFromDb = getUser(username);

        String newPass = editUser.getPassword();
        String newPassConf = editUser.getPasswordConfirm();
        if(!newPass.equals(newPassConf))
            throw new UserException(UserError.PASSWORDS_ARE_NOT_EQUAL);

        userFromDb.setPassword(passwordEncoder.encode(newPass));
        return userRepository.save(userFromDb);
    }

    public void deletePost(Long postId, User admin) throws PostException {
        postService.deletePost(postId, admin);
    }
}
