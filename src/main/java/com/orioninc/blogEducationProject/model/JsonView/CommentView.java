package com.orioninc.blogEducationProject.model.JsonView;

/**
 * This marker interfaces for set serialization view of Comment to Json
 */

public final class CommentView {
    /**
     * View of fields: id, author: [id, username], text, createdDate and lastUpdatedDate
     */
    public interface FullCommentWithOutPost extends UserView.IdUsername, ErrorView.codeMessage{}
}
