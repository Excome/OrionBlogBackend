package com.orioninc.blogEducationProject.model.JsonView;

/***
 * This marker interfaces for set serialization view of User to Json
 */

public final class UserView {
    /**
     * View of fields id, username
     */
    public interface IdUsername extends ErrorView.codeMessage{}

    /**
     *
     */
    public interface CommonInfo extends IdUsername, RoleView.Name{}

    /**
     * Vied of fields: id, email, username, surname, name, privateStatus,
     * birthday, createdDate
     */
    public interface Profile extends CommonInfo{}
}

