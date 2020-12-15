package com.orioninc.blogEducationProject.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.orioninc.blogEducationProject.model.JsonView.PostView;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonView(PostView.CommonPost.class)
    private Long id;
    @ManyToOne
    @JsonView(PostView.CommonPost.class)
    private User author;

    @Size(min = 5, max = 256)
    @JsonView(PostView.CommonPost.class)
    private String topic;
    @NotEmpty
    @Size(min = 10, max = 16384)
    @JsonView(PostView.FullPost.class)
    private String text;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JsonView({PostView.FullPost.class, PostView.PreviewPost.class})
    private Set<Tag> tags = new HashSet<>();

    @CreatedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyy HH:mm")
    @JsonView(PostView.CommonPost.class)
    private Date createdDate;

    @LastModifiedDate
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyy HH:mm")
    @JsonView(PostView.CommonPost.class)
    private Date lastUpdatedDate;

    public Post() {
    }

    @JsonView(PostView.PreviewPost.class)
    public String textPreview(){
        String[] st = this.text.split("(?<=[\\.\\!\\?])");
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<10; i++){
            if(i == st.length)
                break;
            sb.append(st[i]);
        }
        sb.append("...");
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", author=" + author +
                ", topic='" + topic + '\'' +
                '}';
    }
}
