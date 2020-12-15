package com.orioninc.blogEducationProject.service;

import com.orioninc.blogEducationProject.error.UserError;
import com.orioninc.blogEducationProject.exception.UserException;
import com.orioninc.blogEducationProject.model.Role;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.repository.RoleRepository;
import com.orioninc.blogEducationProject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService {

    protected final UserRepository userRepository;
    protected final RoleRepository roleRepository;
    protected final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    public User registrationUser(User newUser) throws UserException {
        User userFromDb = userRepository.findUserByEmail(newUser.getEmail());
        if(userFromDb != null){
            throw new UserException(UserError.EMAIL_IS_ALREADY_USED, "email: "+ newUser.getEmail());
        }
        userFromDb = userRepository.findUserByUsername(newUser.getUsername());
        if(userFromDb != null){
            throw new UserException(UserError.USERNAME_IS_ALREADY_USED, "username: " + newUser.getUsername());
        }
        if(!newUser.getPassword().equals(newUser.getPasswordConfirm())){
            throw new UserException(UserError.PASSWORDS_ARE_NOT_EQUAL);
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        newUser.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));
        newUser.setSurname("");
        newUser.setName("");
        return  userRepository.save(newUser);
    }

    public User getUser(String username) throws UserException {
        User user = userRepository.findUserByUsername(username);

        if (user == null) {
            throw new UserException(UserError.USER_NOT_FOUND, "user: " + username);
        }

        return user;
    }

    public User editProfile(User editProfile, User userFromDb) throws UserException {
        if(!editProfile.getUsername().equals(userFromDb.getUsername()))
            throw new UserException(UserError.USER_NOT_AUTHORIZED);

        userFromDb.setSurname(editProfile.getSurname());
        userFromDb.setName(editProfile.getName());
        userFromDb.setBirthday(editProfile.getBirthday());
        userFromDb.setPrivateStatus(editProfile.isPrivateStatus());

        userRepository.save(userFromDb);
        return userFromDb;
    }

    public User editPassword(User changedUser, User userFromDb) throws UserException {
        if(!changedUser.getUsername().equals(userFromDb.getUsername()))
            throw new UserException(UserError.USER_NOT_AUTHORIZED);

        String newPass = changedUser.getPassword();
        String newPassConf = changedUser.getPasswordConfirm();
        if(!newPass.equals(newPassConf))
            throw new UserException(UserError.PASSWORDS_ARE_NOT_EQUAL);

        userFromDb.setPassword(passwordEncoder.encode(newPass));
        userRepository.save(userFromDb);
        return userFromDb;
    }

    public Page<User> getUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }

    public Page<User> getUsersByUsername(String username, Pageable pageable){
        return userRepository.findAllByUsernameContains(username, pageable);
    }
}
