package com.ecoverde.estateagency.model.service;


import java.time.LocalDateTime;

public class BlogCommentServiceModel extends BaseServiceModel {
    private String title;
    private UserServiceModel author;
    private LocalDateTime publishedAt;
    private String content;

    public BlogCommentServiceModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
