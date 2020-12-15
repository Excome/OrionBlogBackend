package com.orioninc.blogEducationProject.repository;

import com.orioninc.blogEducationProject.model.Subscription;
import com.orioninc.blogEducationProject.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Subscription findByFollowerAndFollow(User follower, User follow);
    Page<Subscription> findAllByFollower(User follower, Pageable pageable);
    Page<Subscription> findAllByFollow(User follow, Pageable pageable);
    /**
     * Only admin method for delete user!!!
     */
    void deleteAllByFollowerOrFollow(User follower, User follow);
}
