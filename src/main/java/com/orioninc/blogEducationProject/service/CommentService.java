package com.orioninc.blogEducationProject.service;

import com.orioninc.blogEducationProject.error.CommentError;
import com.orioninc.blogEducationProject.exception.CommentException;
import com.orioninc.blogEducationProject.exception.PostException;
import com.orioninc.blogEducationProject.model.Comment;
import com.orioninc.blogEducationProject.model.Post;
import com.orioninc.blogEducationProject.model.Tag;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.repository.CommentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private static final Logger LOG = LogManager.getLogger(CommentService.class);

    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostService postService, UserService userService) {
        this.commentRepository = commentRepository;
        this.postService = postService;
        this.userService = userService;
    }

    public Comment createComment(Comment newComment, User author, Long postId) throws CommentException {
        if(author == null)
            throw new CommentException(CommentError.UNAUTHORIZED_USER);

        try {
            Post post = postService.getPost(postId);
            newComment.setAuthor(author);
            newComment.setPost(post);
            return  commentRepository.save(newComment);

        } catch (PostException e) {
            LOG.info("Post: {} Msg: {}", postId, e.getMessage());
            throw new CommentException(CommentError.POST_NOT_FOUND, "postId: " + postId);
        }
    }

    public Comment editComment(Long commentId, Long postId, Comment changedComment, User currentUser) throws CommentException {
        if(currentUser == null)
            throw new CommentException(CommentError.UNAUTHORIZED_USER);

        Comment commentFromDb = getComment(commentId);
        if(commentFromDb == null)
            throw new CommentException(CommentError.COMMENT_NOT_FOUND, "commentId: " + commentId);

        if(!currentUser.isAdmin()) {
            if (!commentFromDb.getAuthor().getUsername().equals(currentUser.getUsername()))
                throw new CommentException(CommentError.USER_IN_NOT_AUTHOR, "user: " + currentUser.getUsername());
        }
        if(!commentFromDb.getPost().getId().equals(postId))
            throw new CommentException(CommentError.POST_NOT_FOUND, "postId: " + postId);

        commentFromDb.setText(changedComment.getText());
        return commentRepository.save(commentFromDb);
    }

    public Comment getComment(Long commentId) throws CommentException {
        Comment commentFromDb = commentRepository.findCommentById(commentId);
        if(commentFromDb == null)
            throw new CommentException(CommentError.COMMENT_NOT_FOUND, "commentId: " + commentId);

        return commentFromDb;
    }

    public Page<Comment> getPostComments(Long postId, Pageable pageable) throws CommentException {
        try {
            Post post = postService.getPost(postId);
            return commentRepository.findCommentsByPost(post, pageable);
        } catch (PostException e) {
            LOG.info("Post: {} Msg: {}",postId, e.getMessage());
            throw new CommentException(CommentError.POST_NOT_FOUND, "postId: " + postId);
        }
    }

    public void deleteComment(Long commentId, User currentUser) throws CommentException {
        if(currentUser == null)
            throw new CommentException(CommentError.UNAUTHORIZED_USER);
        if(commentId == null)
            throw new CommentException(CommentError.COMMENT_NOT_FOUND, "commentId: " + commentId);

        Comment comment = getComment(commentId);
        if(!currentUser.isAdmin()){
            if(!comment.getAuthor().equals(currentUser))
                throw new CommentException(CommentError.USER_IN_NOT_AUTHOR, "user: " + currentUser.getUsername());
        }

        commentRepository.delete(comment);
    }
}
