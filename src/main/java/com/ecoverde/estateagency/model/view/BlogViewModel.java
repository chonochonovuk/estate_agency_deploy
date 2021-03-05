package com.ecoverde.estateagency.model.view;

import com.ecoverde.estateagency.model.service.BlogCommentServiceModel;

import java.time.LocalDate;
import java.util.Set;

public class BlogViewModel {
    private String title;
    private LocalDate createdAt;
    private String content;
    private Set<BlogCommentServiceModel> comments;

    public BlogViewModel() {
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
}
