package com.orioninc.blogEducationProject.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.orioninc.blogEducationProject.exception.PostException;
import com.orioninc.blogEducationProject.model.JsonView.ErrorView;
import com.orioninc.blogEducationProject.model.JsonView.PostView;
import com.orioninc.blogEducationProject.model.Post;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("posts")
public class PostController {
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @JsonView(PostView.PreviewPost.class)
    public ResponseEntity lastPosts(@PageableDefault(size = 15, sort = {"createdDate"},
            direction = Sort.Direction.DESC) Pageable pageable)
    {
        Page<Post> posts = postService.getPosts(pageable);

        return new ResponseEntity(posts.getContent(), HttpStatus.OK);
    }

    @GetMapping("feed")
    @JsonView(PostView.PreviewPost.class)
    public ResponseEntity getPostsByFollower(@AuthenticationPrincipal User currentUser,
                                             @PageableDefault(size = 15, sort = {"createdDate"},
                                                     direction = Sort.Direction.DESC) Pageable pageable) throws PostException {
        Page<Post> posts = postService.getPostsByFollower(currentUser, pageable);

        return new ResponseEntity(posts.getContent(), HttpStatus.OK);
    }

    @GetMapping("search/topic")
    @JsonView(PostView.PreviewPost.class)
    public ResponseEntity getPostsByTopic(@RequestParam String topic,
                                          @PageableDefault(size = 15, sort = {"createdDate"},
                                                  direction = Sort.Direction.DESC) Pageable pageable) throws PostException {
        Page<Post> posts = postService.getPostsByTopic(topic, pageable);

        return new ResponseEntity(posts.getContent(), HttpStatus.OK);
    }

    @GetMapping("search/tag")
    @JsonView(PostView.PreviewPost.class)
    public ResponseEntity getPostsByTag(@RequestParam String tag,
                                        @PageableDefault(size = 15, sort = {"createdDate"},
                                                direction = Sort.Direction.DESC) Pageable pageable) throws PostException {
        Page<Post> posts = postService.getPostsByTag(tag, pageable);

        return new ResponseEntity(posts.getContent(), HttpStatus.OK);
    }

    @GetMapping("search/author")
    @JsonView(PostView.PreviewPost.class)
    public ResponseEntity getPostsByAuthor(@RequestParam String username,
                                           @PageableDefault(size = 15, sort = {"createdDate"},
                                                   direction = Sort.Direction.DESC) Pageable pageable) throws PostException {
        Page<Post> posts = postService.getPostsByAuthor(username, pageable);

        return new ResponseEntity(posts.getContent(), HttpStatus.OK);
    }


    @PostMapping
    @JsonView(PostView.FullPost.class)
    public ResponseEntity createPost(@RequestBody @Valid Post newPost,
                                     @AuthenticationPrincipal User currentUser) throws PostException {
        newPost = postService.createPost(newPost, currentUser);

        return new ResponseEntity(newPost, HttpStatus.OK);
    }

    @GetMapping("{postId}")
    @JsonView(PostView.FullPost.class)
    public ResponseEntity getPost(@PathVariable Long postId) throws PostException {
        Post postFromDb = postService.getPost(postId);

        return new ResponseEntity(postFromDb, HttpStatus.OK);
    }

    @PutMapping("{postId}")
    @JsonView(PostView.FullPost.class)
    public ResponseEntity editPost(@PathVariable Long postId,
                                   @RequestBody @Valid Post changedPost,
                                   @AuthenticationPrincipal User currentUser) throws PostException {
        changedPost = postService.editPost(postId, changedPost, currentUser);

        return new ResponseEntity(changedPost, HttpStatus.OK);
    }

    @DeleteMapping("{postId}")
    @JsonView(ErrorView.codeMessage.class)
    public ResponseEntity deletePost(@PathVariable Long postId,
                                     @AuthenticationPrincipal User currentUser) throws PostException {
        postService.deletePost(postId, currentUser);

        return new ResponseEntity(HttpStatus.OK);
    }
}
