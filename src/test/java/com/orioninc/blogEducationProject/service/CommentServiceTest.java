package com.orioninc.blogEducationProject.service;

import com.orioninc.blogEducationProject.error.PostError;
import com.orioninc.blogEducationProject.exception.CommentException;
import com.orioninc.blogEducationProject.exception.PostException;
import com.orioninc.blogEducationProject.model.Comment;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = DataConfigTest.class, loader = AnnotationConfigContextLoader.class)
@Transactional
@Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-posts.sql"})
@Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CommentServiceTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Mock
    private PostService postService;
    @Mock
    private UserService userService;

    private CommentService commentService;

    @BeforeEach
    void setUp() {
        this.commentService = new CommentService(this.commentRepository, this.postService, this.userService);
    }

    @Test
    void createComment_success() throws CommentException, PostException {
        User user = userRepository.findUserById(1L);
        Post post = postRepository.findPostById(1L);
        Comment comment = new Comment();
        comment.setText("comment");
        when(postService.getPost(1L)).thenReturn(post);
        assertThat(commentService.createComment(comment, user, post.getId()))
                .isNotNull()
                .matches(comment1 -> comment.getId() != null)
                .hasFieldOrPropertyWithValue("author", user)
                .hasFieldOrPropertyWithValue("text", "comment");
    }

    @Test
    void createComment_throwUnauthorizedUser(){
        Comment comment = new Comment();
        comment.setText("comment");
        assertThatExceptionOfType(CommentException.class)
                .isThrownBy(() -> commentService.createComment(comment, null, 1l))
                .withMessageContaining("Unauthorized user");
    }

    @Test
    void createComment_throwPostNotFound() throws PostException {
        Comment comment = new Comment();
        comment.setText("comment");
        User user = userRepository.findUserById(1L);
        when(postService.getPost(10L)).thenThrow(new PostException(PostError.POST_NOT_FOUND));
        assertThatExceptionOfType(CommentException.class)
                .isThrownBy(() -> commentService.createComment(comment, user, 10l))
                .withMessageContaining("postId: 10");
    }

    @Test
    @Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-post-comment.sql"})
    @Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getComment_success() throws CommentException {
        assertThat(commentService.getComment(1L))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("text", "comment");
    }

    @Test
    void getComment_throwCommentNotFound() throws CommentException {
        assertThatExceptionOfType(CommentException.class)
                .isThrownBy(() -> commentService.getComment(10L))
                .withMessageContaining("commentId: 10");
    }


    @Test
    @Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-post-comment.sql"})
    @Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void editComment_success() throws CommentException {
        Comment editComment = new Comment();
        editComment.setText("editComment");
        User user = userRepository.findUserById(1L);
        assertThat(commentService.editComment(1L, 1L, editComment, user))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("author", user)
                .hasFieldOrPropertyWithValue("text", "editComment");
    }

    @Test
    void editComment_throwUnauthorizedUser(){
        Comment editComment = new Comment();
        editComment.setText("editComment");
        assertThatExceptionOfType(CommentException.class)
                .isThrownBy(() -> commentService.editComment(1l, 1l, editComment, null))
                .withMessageContaining("Unauthorized user");
    }

    @Test
    void editComment_throwCommentNotFound(){
        Comment editComment = new Comment();
        editComment.setText("editComment");
        User user = userRepository.findUserById(1L);
        assertThatExceptionOfType(CommentException.class)
                .isThrownBy(() -> commentService.editComment(10l, 1l, editComment, user))
                .withMessageContaining("commentId: 10");
    }

    @Test
    @Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-post-comment.sql"})
    @Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void editComment_throwUserIsNotAuthor(){
        Comment editComment = new Comment();
        editComment.setText("editComment");
        User user = userRepository.findUserById(2L);
        assertThatExceptionOfType(CommentException.class)
                .isThrownBy(() -> commentService.editComment(1l, 1l, editComment, user))
                .withMessageContaining("user: test");
    }

    @Test
    @Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-post-comment.sql"})
    @Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void editComment_throwPostNotFound(){
        Comment editComment = new Comment();
        editComment.setText("editComment");
        User user = userRepository.findUserById(1L);
        assertThatExceptionOfType(CommentException.class)
                .isThrownBy(() -> commentService.editComment(1l, 10l, editComment, user))
                .withMessageContaining("postId: 10");
    }

    @Test
    @Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-post-comment.sql"})
    @Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getPostComments_success() throws CommentException, PostException {
        Pageable pageable = PageRequest.of(0, 20);
        when(postService.getPost(1L)).thenReturn(postRepository.findPostById(1L));
        List<Comment> comments = commentService.getPostComments(1l, pageable).getContent();
        assertThat(comments)
                .hasSize(1)
                .allMatch(comment -> comment.getPost().getId() == 1L);
    }

    @Test
    void getPostComments_throwPostNotFound() throws PostException {
        Pageable pageable = PageRequest.of(0, 20);
        when(postService.getPost(1L)).thenThrow(new PostException(PostError.POST_NOT_FOUND));
        assertThatExceptionOfType(CommentException.class)
                .isThrownBy(() -> commentService.getPostComments(1L, pageable))
                .withMessageContaining("postId: 1");
    }

    @Test
    void deleteComment_success() throws CommentException {
        User user = userRepository.findUserById(1L);
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setText("test");
        comment = commentRepository.save(comment);

        commentService.deleteComment(comment.getId(), user);
        assertThat(commentRepository.findCommentById(comment.getId())).isNull();
    }

    @Test
    void deleteComment_throwUnauthorizedUser(){
        assertThatExceptionOfType(CommentException.class)
                .isThrownBy(() -> commentService.deleteComment(1L, null))
                .withMessageContaining("Unauthorized user");
    }

    @Test
    void deleteComment_throwCommentNotFound(){
        User user = userRepository.findUserById(1L);
        assertThatExceptionOfType(CommentException.class)
                .isThrownBy(() -> commentService.deleteComment(null, user))
                .withMessageContaining("commentId: null");
    }

    @Test
    @Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-post-comment.sql"})
    @Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteComment_throwUserIsNotAuthor(){
        User user = userRepository.findUserById(2L);

        assertThatExceptionOfType(CommentException.class)
                .isThrownBy(() -> commentService.deleteComment(1L, user))
                .withMessageContaining("user: test");
    }
}