package com.orioninc.blogEducationProject.model.JsonView;

/**
 *This marker interfaces for set serialization view of Post to Json
 */
public final class PostView {
    /**
     *
     */
    public interface CommonPost extends UserView.IdUsername, ErrorView.codeMessage{}

    public interface PreviewPost extends CommonPost, TagView.Name {}

    /**
     * View of fields: id, author: [id, username], topic, text, tags, createdDate and lastUpdatedDate
     */
    public interface FullPost extends CommonPost, TagView.Name {}
}
