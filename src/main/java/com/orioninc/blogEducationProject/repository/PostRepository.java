package com.orioninc.blogEducationProject.repository;

import com.orioninc.blogEducationProject.model.Post;
import com.orioninc.blogEducationProject.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Post findPostById(Long id);
    Page<Post> findPostsByTopicContains(String topic, Pageable pageable);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findPostsByAuthorUsername(String username, Pageable pageable);
    Page<Post> findPostsByTagsName(String name, Pageable pageable);
    @Query(
            "select p from Post p " +
                    "inner join Subscription s on s.follow.id = p.author.id " +
                    "where s.follower.username = :username"
    )
    Page<Post> findPostsByFollower(@Param("username") String username, Pageable pageable);
    /**
     * Only admin method for delete user!!!
     */
    void deleteAllByAuthor(User author);
}
