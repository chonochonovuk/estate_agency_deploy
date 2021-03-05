package com.ecoverde.estateagency.model.service;


import java.time.LocalDate;
import java.util.Set;


public class BlogServiceModel extends BaseServiceModel{
    private UserServiceModel author;
    private String title;
    private LocalDate createdAt;
    private String content;
    private Set<BlogCommentServiceModel> comments;
    private boolean isArchived;
    private boolean isLockForComments;

    public BlogServiceModel() {
    }

    public UserServiceModel getAuthor() {
        return author;
    }

    public void setAuthor(UserServiceModel author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<BlogCommentServiceModel> getComments() {
        return comments;
    }

    public void setComments(Set<BlogCommentServiceModel> comments) {
        this.comments = comments;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public boolean isLockForComments() {
        return isLockForComments;
    }

    public void setLockForComments(boolean lockForComments) {
        isLockForComments = lockForComments;
    }
}
