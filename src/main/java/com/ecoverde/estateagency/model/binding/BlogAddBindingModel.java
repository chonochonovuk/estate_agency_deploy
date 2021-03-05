package com.ecoverde.estateagency.model.binding;

import com.ecoverde.estateagency.model.entity.BlogComment;
import com.ecoverde.estateagency.model.service.UserServiceModel;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.Set;

public class BlogAddBindingModel {
    private UserServiceModel author;
    private String title;
    private LocalDate createdAt;
    private String content;

    public BlogAddBindingModel() {
    }

    public UserServiceModel getAuthor() {
        return author;
    }

    public void setAuthor(UserServiceModel author) {
        this.author = author;
    }

    @Length(min = 6, message = "Minimum 6 characters length")
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

    @Length(min = 30, message = "Minimum 30 characters length")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
