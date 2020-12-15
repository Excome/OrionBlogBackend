package com.orioninc.blogEducationProject.service;

import com.orioninc.blogEducationProject.error.TagError;
import com.orioninc.blogEducationProject.exception.TagException;
import com.orioninc.blogEducationProject.model.Post;
import com.orioninc.blogEducationProject.model.Tag;
import com.orioninc.blogEducationProject.model.User;
import com.orioninc.blogEducationProject.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag getTag(String tag) throws TagException {
        if(tag == null)
            throw new TagException(TagError.INVALID_SEARCH_ARGUMENT, "name: " + tag);

        Tag tagFromDb = tagRepository.findTagByName(tag);
        if(tagFromDb == null)
            throw new TagException(TagError.TAG_NOT_FOUND, "tag: " + tag);

        return tagFromDb;
    }

    public Set<Tag> getTagsForPost(Post post){
        Set<Tag> tags = new HashSet<>();
        for(Tag tag : post.getTags()){
            if(tagRepository.findTagByName(tag.getName()) == null){
                tagRepository.save(tag);
            }
            tags.add(tagRepository.findTagByName(tag.getName()));
        }

        return tags;
    }



    /**
     * todo tag edit/delete for admin
     */

    public Page<Tag> getTags(Pageable pageable){
        return tagRepository.findAll(pageable);
    }

    public Tag editTag(String tagName, Tag editTag, User currentUser) throws TagException {
        Tag tagFromDb = getTag(tagName);

        if(!currentUser.isAdmin())
            throw new TagException(TagError.USER_IS_NOT_ADMIN, "user: " + currentUser.getUsername());

        tagFromDb.setName(editTag.getName());
        tagRepository.save(tagFromDb);

        return tagFromDb;
    }

    public void deleteTag(String tagName, User currentUser) throws TagException {
        Tag tagFromDb = getTag(tagName);
        if(!currentUser.isAdmin())
            throw new TagException(TagError.USER_IS_NOT_ADMIN, "user: " + currentUser.getUsername());

        tagRepository.delete(tagFromDb);
    }
}
