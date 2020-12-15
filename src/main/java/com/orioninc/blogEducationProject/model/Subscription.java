package com.orioninc.blogEducationProject.model;

import com.fasterxml.jackson.annotation.JsonView;
import com.orioninc.blogEducationProject.model.JsonView.SubscriptionView;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "subscriptions")
@EntityListeners(AuditingEntityListener.class)
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @NotNull
    @JsonView(SubscriptionView.FollowerFollow.class)
    private User follower;

    @ManyToOne
    @NotNull
    @JsonView(SubscriptionView.FollowerFollow.class)
    private User follow;

    @CreatedDate
    @DateTimeFormat(pattern = "dd.MM.yyyy hh:mm", style = "dd.MM.yyyy hh:mm")
    private Date createdDate;

    public Subscription() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollow() {
        return follow;
    }

    public void setFollow(User subscription) {
        this.follow = subscription;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "id=" + id +
                ", follower=" + follower +
                ", follow=" + follow +
                '}';
    }
}
