package com.orioninc.blogEducationProject.service;

import com.orioninc.blogEducationProject.controller.CommentController;
import com.orioninc.blogEducationProject.controller.PostController;
import com.orioninc.blogEducationProject.controller.UserController;
import com.orioninc.blogEducationProject.error.PostError;
import com.orioninc.blogEducationProject.error.UserError;
import com.orioninc.blogEducationProject.exception.PostException;
import com.orioninc.blogEducationProject.exception.UserException;
import com.orioninc.blogEducationProject.model.Post;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.repository.CommentRepository;
import com.orioninc.blogEducationProject.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagService tagService;

    @Autowired
    public PostService(PostRepository postRepository, CommentRepository commentRepository, TagService tagService) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.tagService = tagService;
    }

    public Post createPost(Post newPost, User currentUser) throws PostException {
        if(currentUser == null)
            throw new PostException(PostError.UNAUTHORIZED_USER);

        newPost.setTags(tagService.getTagsForPost(newPost));
        newPost.setAuthor(currentUser);

        return postRepository.save(newPost);
    }

    public Post editPost(Long postId, Post changedPost, User currentUser) throws PostException {
        if(currentUser == null)
            throw new PostException(PostError.UNAUTHORIZED_USER);

        Post postFromDb = postRepository.findPostById(postId);
        if(postFromDb == null)
            throw new PostException(PostError.POST_NOT_FOUND, "postId: " + postId);

        if(!currentUser.isAdmin()) {
            if (!postFromDb.getAuthor().equals(currentUser))
                throw new PostException(PostError.USER_IS_NOT_AUTHOR, "username: " + currentUser.getUsername());
        }

        postFromDb.setTopic(changedPost.getTopic());
        postFromDb.setText(changedPost.getText());
        postFromDb.setTags(tagService.getTagsForPost(changedPost));
        postRepository.save(postFromDb);

        return postFromDb;
    }

    @Transactional
    public void deletePost(Long postId, User currentUser) throws PostException {
        if(currentUser == null)
            throw new PostException(PostError.UNAUTHORIZED_USER);

        Post postFromDb = postRepository.findPostById(postId);
        if(postFromDb == null)
            throw new PostException(PostError.POST_NOT_FOUND);

        if(!currentUser.isAdmin()) {
            if (!postFromDb.getAuthor().getUsername().equals(currentUser.getUsername()))
                throw new PostException(PostError.USER_IS_NOT_AUTHOR);
        }

        commentRepository.deleteAllByPostId(postId);
        postRepository.delete(postFromDb);
    }

    public Post getPost(Long postId) throws PostException {
        Post postFromDb = postRepository.findPostById(postId);
        if(postFromDb == null){
            throw new PostException(PostError.POST_NOT_FOUND, "postId: "+postId);
        }
        return postFromDb;
    }

    public Page<Post> getPosts(Pageable pageable){
        return postRepository.findAll(pageable);
    }

    public Page<Post> getPostsByTopic(String topic, Pageable pageable) throws PostException {
        if(topic == null ){
            throw new PostException(PostError.INVALID_SEARCH_ARGUMENT, "topic: " + topic);
        }
        return postRepository.findPostsByTopicContains(topic, pageable);
    }

    public Page<Post> getPostsByAuthor(String username, Pageable pageable) throws PostException {
        if(username == null)
            throw new PostException(PostError.INVALID_SEARCH_ARGUMENT, "username: " + username);

        return postRepository.findPostsByAuthorUsername(username, pageable);
    }

    public Page<Post> getPostsByTag(String tag, Pageable pageable) throws PostException {
        if(tag == null)
            throw new PostException(PostError.INVALID_SEARCH_ARGUMENT, "tag: " + tag);

        return postRepository.findPostsByTagsName(tag, pageable);
    }

    public Page<Post> getPostsByFollower(User currentUser, Pageable pageable) throws PostException {
        if(currentUser ==  null)
            throw new PostException(PostError.UNAUTHORIZED_USER);

        return postRepository.findPostsByFollower(currentUser.getUsername(), pageable);
    }

    public void deleteAllUserPosts(User author) throws UserException {
        /**
         * Only admin method for delete user!!!
         */
        if(author == null)
            throw new UserException(UserError.USER_NOT_FOUND);

        commentRepository.deleteAllByPostAuthor(author);
        commentRepository.deleteAllByAuthor(author);
        postRepository.deleteAllByAuthor(author);
    }
}
