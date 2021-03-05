package com.ecoverde.estateagency.service.impl;

import com.ecoverde.estateagency.model.entity.BlogComment;
import com.ecoverde.estateagency.model.service.BlogCommentServiceModel;
import com.ecoverde.estateagency.repositories.BlogCommentRepository;
import com.ecoverde.estateagency.service.BlogCommentService;
import com.ecoverde.estateagency.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
public class BlogCommentServiceImpl implements BlogCommentService {
    private final BlogCommentRepository blogCommentRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public BlogCommentServiceImpl(BlogCommentRepository blogCommentRepository, UserService userService, ModelMapper modelMapper) {
        this.blogCommentRepository = blogCommentRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<BlogComment> findAllByAuthorUsername(String username) {
        return this.blogCommentRepository.findAllByAuthorUsername(username);
    }

    @Transactional
    @Override
    public void deleteByTitle(String title) {
        if (this.findByTitle(title) != null){
            this.blogCommentRepository.deleteByTitle(title);
        }
    }

    @Transactional
    @Override
    public void deleteAllByAuthorUsername(String username) {
          if (this.findAllByAuthorUsername(username) != null){
             Set<BlogComment> toDelete = this.findAllByAuthorUsername(username);
              for (BlogComment bc:toDelete) {
                  this.deleteByTitle(bc.getTitle());
              }
          }
    }

    @Override
    public BlogCommentServiceModel addBlogComment(BlogCommentServiceModel blogCommentServiceModel) {
        blogCommentServiceModel.setAuthor(this.userService.findByUsername(blogCommentServiceModel.getAuthor().getUsername()));
        BlogComment blogComment = this.modelMapper.map(blogCommentServiceModel,BlogComment.class);
        if (this.findByTitle(blogCommentServiceModel.getTitle()) == null){
            this.blogCommentRepository.saveAndFlush(blogComment);
        }
        return this.modelMapper.map(blogComment,BlogCommentServiceModel.class);
    }

    @Override
    public BlogCommentServiceModel findByTitle(String title) {
        return this.blogCommentRepository.findByTitle(title).map(blogComment -> this.modelMapper.map(blogComment,BlogCommentServiceModel.class))
                .orElse(null);
    }

}
