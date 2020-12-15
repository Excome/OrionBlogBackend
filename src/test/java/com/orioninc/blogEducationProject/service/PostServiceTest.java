package com.orioninc.blogEducationProject.service;

import com.orioninc.blogEducationProject.exception.PostException;
import com.orioninc.blogEducationProject.model.Post;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.repository.CommentRepository;
import com.orioninc.blogEducationProject.repository.PostRepository;
import com.orioninc.blogEducationProject.repository.UserRepository;
import com.orioninc.blogEducationProject.service.config.DataConfigTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = DataConfigTest.class, loader = AnnotationConfigContextLoader.class)
@Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-posts.sql"})
@Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Transactional
class PostServiceTest {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private TagService tagService;

    private PostService postService;

    @BeforeEach
    void setUp() {
        postService = new PostService(postRepository, commentRepository, tagService);
    }

    @Test
    void createPost_success() throws PostException {
        User currentUser = new User();
        Post post = new Post();
        when(tagService.getTagsForPost(post)).thenReturn(new HashSet<>());
        assertThat(postService.createPost(post, currentUser))
                .isNotNull()
                .hasFieldOrProperty("id")
                .hasFieldOrProperty("author")
                .hasFieldOrProperty("tags");
    }

    @Test
    void createPost_throwUnauthorizedUser(){
        Post post = new Post();
        assertThatExceptionOfType(PostException.class)
                .isThrownBy(() -> postService.createPost(post, null))
                .withMessageContaining("Unauthorized user");
    }

    @Test
    void editPost_success() throws PostException {
        User currentUser = userRepository.findUserById(1l);

        Post editPost = new Post();
        editPost.setTopic("testTopic");
        editPost.setText("testText");
        editPost.setTags(new HashSet<>());
        when(tagService.getTagsForPost(editPost)).thenReturn(new HashSet<>());
        assertThat(postService.editPost(1L, editPost, currentUser))
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("author", currentUser)
                .hasFieldOrPropertyWithValue("topic", "testTopic")
                .hasFieldOrPropertyWithValue("text", "testText");

    }

    @Test
    void editPost_throwUnauthorizedUser(){
        assertThatExceptionOfType(PostException.class)
                .isThrownBy(() -> postService.editPost(2l,  new Post(), null))
                .withMessageContaining("Unauthorized user");
    }

    @Test
    void editPost_throwPostNotFound(){
        assertThatExceptionOfType(PostException.class)
                .isThrownBy(() -> postService.editPost(1000L,  new Post(), new User()))
                .withMessageContaining("postId: 1000");
    }

    @Test
    void editPost_throwUserIsNotAuthor(){
        User currentUser = userRepository.findUserById(2l);

        assertThatExceptionOfType(PostException.class)
                .isThrownBy(() -> postService.editPost(1l,  new Post(),currentUser ))
                .withMessageContaining("username: test");
    }

    @Test
    void getPost_success() throws PostException {
        assertThat(postService.getPost(1L)).isNotNull();
    }

    @Test
    void getPost_throwPostNotFound() {
        assertThatExceptionOfType(PostException.class)
                .isThrownBy(() -> postService.getPost(10l))
                .withMessageContaining("postId: 10");
    }

    @Test
    void getPosts_success() {
        Pageable pageable = PageRequest.of(0, 15);
        List<Post> posts = postService.getPosts(pageable).getContent();
        assertThat(posts)
                .isNotEmpty()
                .hasSize(2)
                .allMatch(post -> !post.getId().equals(null));
    }

    @Test
    void getPosts_emptyPage(){
        Pageable pageable = PageRequest.of(1, 15);
        List<Post> posts = postService.getPosts(pageable).getContent();
        assertThat(posts)
                .isEmpty();
    }

    @Test
    void getPostsByTopic_success() throws PostException {
        Pageable pageable = PageRequest.of(0, 15);
        List<Post> posts = postService.getPostsByTopic("greeting", pageable).getContent();
        assertThat(posts)
                .isNotEmpty()
                .hasSize(1)
                .allMatch(post -> post.getTopic().contains("greeting"));
    }

    @Test
    void getPostsByTopic_throwInvalidSearchArgument(){
        Pageable pageable = PageRequest.of(0, 15);
        assertThatExceptionOfType(PostException.class)
                .isThrownBy(() -> postService.getPostsByTopic(null, pageable))
                .withMessageContaining("Invalid search argument");
    }

    @Test
    void getPostsByAuthor_success() throws PostException {
        Pageable pageable = PageRequest.of(0, 15);
        List<Post> posts = postService.getPostsByAuthor("user", pageable).getContent();
        assertThat(posts)
                .isNotEmpty()
                .hasSize(1)
                .allMatch(post -> post.getAuthor().getUsername().equals("user"));
    }

    @Test
    void getPostsByAuthor_throwInvalidSearchArgument() throws PostException {
        Pageable pageable = PageRequest.of(0, 15);
        assertThatExceptionOfType(PostException.class)
                .isThrownBy(() -> postService.getPostsByAuthor(null, pageable))
                .withMessageContaining("Invalid search argument");
    }

    @Test
    @Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-posts.sql", "classpath:sql/create-tag.sql"})
    @Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getPostsByTag() throws PostException {
        Pageable pageable = PageRequest.of(0, 15);
        List<Post> posts = postService.getPostsByTag("test tag", pageable).getContent();
        assertThat(posts)
                .isNotEmpty()
                .hasSize(1)
                .allMatch(post -> post.getTags().stream().allMatch(tag -> tag.getName().contains("tag")));
    }
}