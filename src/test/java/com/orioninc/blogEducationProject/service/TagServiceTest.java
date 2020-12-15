package com.orioninc.blogEducationProject.service;

import com.orioninc.blogEducationProject.exception.TagException;
import com.orioninc.blogEducationProject.model.Post;
import com.orioninc.blogEducationProject.model.Tag;
import com.orioninc.blogEducationProject.repository.TagRepository;
import com.orioninc.blogEducationProject.service.config.DataConfigTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = DataConfigTest.class, loader = AnnotationConfigContextLoader.class)
@Transactional
class TagServiceTest {
    @Autowired
    private TagRepository tagRepository;

    private TagService tagService;

    @BeforeEach
    void setUp() {
        this.tagService = new TagService(tagRepository);
    }

    @Test
    @Sql(scripts = {"classpath:sql/create-users.sql", "classpath:sql/create-posts.sql", "classpath:sql/create-tag.sql"})
    @Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getTag_success() throws TagException {
        assertThat(tagService.getTag("test tag"))
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("name", "test tag");
    }

    @Test
    void getTag_throwInvalidSearchArgument() {
        assertThatExceptionOfType(TagException.class)
                .isThrownBy(() -> tagService.getTag(null))
                .withMessageContaining("Invalid tag name");
    }

    @Test
    void getTag_TagNotFound() {
        assertThatExceptionOfType(TagException.class)
                .isThrownBy(() -> tagService.getTag("orioninc"))
                .withMessageContaining("tag: orioninc");
    }

    @Test
    @Sql(scripts = "classpath:sql/clear-tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getTagsForPost_success() {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag(null, "tag2"));
        tags.add(new Tag(null, "tag3"));
        tags.add(new Tag(null, "tag4"));

        Post post = new Post();
        post.setTags(tags);
        assertThat(tagService.getTagsForPost(post))
                .isNotEmpty()
                .allMatch(tag -> tag.equals(tagRepository.findTagByName(tag.getName())));
    }

}