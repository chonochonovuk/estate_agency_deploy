package com.ecoverde.estateagency.service;

import com.ecoverde.estateagency.model.entity.BlogComment;
import com.ecoverde.estateagency.model.service.BlogCommentServiceModel;
import com.ecoverde.estateagency.model.service.BlogServiceModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface BlogCommentService {

    Set<BlogComment> findAllByAuthorUsername(String username);

    void deleteByTitle(String title);

    void deleteAllByAuthorUsername(String username);

    BlogCommentServiceModel addBlogComment(BlogCommentServiceModel blogCommentServiceModel);

    BlogCommentServiceModel findByTitle(String title);
}
