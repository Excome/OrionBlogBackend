package com.orioninc.blogEducationProject.repository;

import com.orioninc.blogEducationProject.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findRoleByNameLike(String role);
    Role findRoleByNameContains(String name);
}
