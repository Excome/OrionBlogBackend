package com.orioninc.blogEducationProject.repository;

import com.orioninc.blogEducationProject.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findTagByName(String name);
    Page<Tag> findAll(Pageable pageable);
}
