package com.orioninc.blogEducationProject.repository;

import com.orioninc.blogEducationProject.model.Comment;
import com.orioninc.blogEducationProject.model.Post;
import com.orioninc.blogEducationProject.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Comment findCommentById(Long commentId);
    Page<Comment> findCommentsByPost(Post post, Pageable pageable);
    void deleteAllByPostId(Long postId);
    /**
     * Only admin method for delete user!!!
     */
    void deleteAllByPostAuthor(User author);
    /**
     * Only admin method for delete user!!!
     */
    void deleteAllByAuthor(User author);
}
