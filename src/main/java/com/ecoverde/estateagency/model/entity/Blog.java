package com.ecoverde.estateagency.model.entity;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "blogs")
public class Blog extends BaseEntity {
    private User author;
    private String title;
    private LocalDate createdAt;
    private String content;
    private Set<BlogComment> comments;
    private boolean isArchived;
    private boolean isLockForComments;

    public Blog() {
    }

    @OneToOne
    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    @Column(name = "title",nullable = false,unique = true)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "createdAt",nullable = false)
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "content",nullable = false,columnDefinition = "TEXT")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @OneToMany(fetch = FetchType.EAGER)
    public Set<BlogComment> getComments() {
        return comments;
    }

    public void setComments(Set<BlogComment> comments) {
        this.comments = comments;
    }

    @Column(name = "isArchived", nullable = false)
    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    @Column(name = "isLockForComments",nullable = false)
    public boolean isLockForComments() {
        return isLockForComments;
    }

    public void setLockForComments(boolean lockForComments) {
        isLockForComments = lockForComments;
    }
}
