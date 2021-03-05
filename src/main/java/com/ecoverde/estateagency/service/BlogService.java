package com.ecoverde.estateagency.service;

import com.ecoverde.estateagency.model.entity.Blog;
import com.ecoverde.estateagency.model.service.BlogCommentServiceModel;
import com.ecoverde.estateagency.model.service.BlogServiceModel;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface BlogService {
    void blogsInit();

    Set<Blog> findAllBlogsByAuthorUsername(String username);

    void deleteAllByAuthorUsername(String username);

    void deleteBlogByTitle(String title);

    BlogServiceModel addBlog(BlogServiceModel blogServiceModel);

    Set<BlogServiceModel> findAll();

    BlogServiceModel findByTitle(String title);

    void changeBlogStatus(String title, String status);

    void addBlogComment(String blogTitle, String blogCommentTitle);

}
