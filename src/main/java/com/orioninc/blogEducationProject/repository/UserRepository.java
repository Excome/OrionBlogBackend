package com.orioninc.blogEducationProject.repository;

import com.orioninc.blogEducationProject.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    User findUserById(Long userId);
    Page<User> findAll(Pageable pageable);
    Page<User> findAllByUsernameContains(String username, Pageable pageable);
}
