package com.orioninc.blogEducationProject.model.JsonView;

/**
 * This marker interfaces for set serialization view of Subscription to Json
 */
public final class SubscriptionView {
    /**
     * View of field follower and follow: {id, username}
     */
    public interface FollowerFollow extends UserView.IdUsername, ErrorView.codeMessage{}
}
