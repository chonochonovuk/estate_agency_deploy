package com.ecoverde.estateagency.model.binding;

import com.ecoverde.estateagency.model.entity.User;
import com.ecoverde.estateagency.model.service.UserServiceModel;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

public class BlogCommentBindingModel {
    private String commentTitle;
    private UserServiceModel author;
    private LocalDateTime publishedAt;
    private String content;

    public BlogCommentBindingModel() {
    }

    @Length(min = 6, message = "Minimum 6 characters length")
    public String getCommentTitle() {
        return commentTitle;
    }

    public void setCommentTitle(String commentTitle) {
        this.commentTitle = commentTitle;
    }

    public UserServiceModel getAuthor() {
        return author;
    }

    public void setAuthor(UserServiceModel author) {
        this.author = author;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    @Length(min = 30, message = "Minimum 30 characters length")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
