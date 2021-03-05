package com.ecoverde.estateagency.service.impl;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.ecoverde.estateagency.model.entity.Blog;
import com.ecoverde.estateagency.model.entity.Role;
import com.ecoverde.estateagency.model.entity.User;
import com.ecoverde.estateagency.model.service.BlogCommentServiceModel;
import com.ecoverde.estateagency.model.service.BlogServiceModel;
import com.ecoverde.estateagency.repositories.BlogRepository;
import com.ecoverde.estateagency.service.BlogCommentService;
import com.ecoverde.estateagency.service.BlogService;
import com.ecoverde.estateagency.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final BlogCommentService blogCommentService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository, BlogCommentService blogCommentService, UserService userService, ModelMapper modelMapper) {
        this.blogRepository = blogRepository;
        this.blogCommentService = blogCommentService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void blogsInit() {
        if(this.blogRepository.count() == 0){

        BlogServiceModel post1 = new BlogServiceModel();
        post1.setAuthor(this.userService.findByUsername("joanna"));
        post1.setTitle("Living In Bulgaria, A Village Life");
        post1.setContent("My name is Joanna. I came to Bulgaria 6 years ago, fell in love with the people and the country with all its traditions, bought a home, closed my ethnic shop in UK and moved here permanently 3 years ago. I am married and I have 3 children, 3 step children and 6 grand children.");
        post1.setCreatedAt(LocalDate.parse("2020-01-12", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        BlogCommentServiceModel bc2 = new BlogCommentServiceModel();
        bc2.setAuthor(this.userService.findByUsername("pesho"));
        bc2.setTitle("England is glorious");
        bc2.setContent("Apart from being with family & the fact England is glorious in the sunshine the other great thing about being here, at this time, is the Queens Diamond  ");
        bc2.setPublishedAt(LocalDateTime.parse("2020-02-11T12:34", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        Set<BlogCommentServiceModel> comments1 = new HashSet<>();
        comments1.add(bc2);
        post1.setComments(comments1);
        post1.setArchived(false);
        post1.setLockForComments(false);

        BlogServiceModel post2 = new BlogServiceModel();
        post2.setAuthor(this.userService.findByUsername("jana66"));
        post2.setTitle("Home Sweet Home, Viva Bulgaria");
        post2.setContent("My name is Joanna. I came to Bulgaria 6 years ago, fell in love with the people and the country with all its traditions, bought a home, closed my ethnic shop in UK and moved here permanently 3 years ago. I am married and I have 3 children, 3 step children and 6 grand children.");
        post2.setCreatedAt(LocalDate.parse("2020-08-07", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        BlogCommentServiceModel bc = new BlogCommentServiceModel();
        bc.setAuthor(this.userService.findByUsername("pesho"));
        bc.setTitle("Again in Bulgaria");
        bc.setContent("What can I say, now we are all out & about again in Bulgaria. Now we have all shaken off the snow & hibernation blues, ");
        bc.setPublishedAt(LocalDateTime.parse("2020-08-13T19:34", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        Set<BlogCommentServiceModel> comments2 = new HashSet<>();
        comments2.add(bc);
        post2.setComments(comments2);
        post2.setArchived(false);
        post2.setLockForComments(false);
        this.addBlog(post1);
        this.addBlog(post2);
        }
    }

    @Override
    public Set<Blog> findAllBlogsByAuthorUsername(String username) {
        return this.blogRepository.findAllByAuthorUsername(username);
    }

    @Transactional
    @Override
    public void deleteAllByAuthorUsername(String username) {
       if (this.findAllBlogsByAuthorUsername(username) != null){
          Set<Blog> toDelete = this.findAllBlogsByAuthorUsername(username);
           for (Blog b:toDelete) {
               this.deleteBlogByTitle(b.getTitle());
           }
       }
    }

    @Transactional
    @Override
    public void deleteBlogByTitle(String title) {
       if (this.findByTitle(title) != null){
           this.blogRepository.deleteByTitle(title);
       }
    }

    @Override
    public BlogServiceModel addBlog(BlogServiceModel blogServiceModel) {
        if (blogServiceModel.getComments() != null){
            blogServiceModel.getComments().forEach(blogCommentService::addBlogComment);
            blogServiceModel.setComments(blogServiceModel.getComments().stream().
                    map(blogCommentServiceModel -> this.blogCommentService.findByTitle(blogCommentServiceModel.getTitle())).collect(Collectors.toSet()));
        }
       blogServiceModel.setArchived(false);
       blogServiceModel.setLockForComments(false);
        Blog blog = this.modelMapper.map(blogServiceModel,Blog.class);
        if (this.blogRepository.findByTitle(blogServiceModel.getTitle()).isEmpty()){
            this.blogRepository.saveAndFlush(blog);
        }
        return this.modelMapper.map(blog,BlogServiceModel.class);
    }

    @Override
    public Set<BlogServiceModel> findAll() {
        return this.blogRepository.findAll().stream().map(blog -> this.modelMapper.map(blog,BlogServiceModel.class)).collect(Collectors.toSet());
    }

    @Override
    public BlogServiceModel findByTitle(String title) {
        return this.blogRepository.findByTitle(title).map(blog -> this.modelMapper.map(blog,BlogServiceModel.class)).orElse(null);
    }

    @Override
    public void changeBlogStatus(String title, String status) {
        BlogServiceModel bsm = this.findByTitle(title);
        switch (status) {
            case "ARCHIVED" -> bsm.setArchived(true);

            case "UNARCHIVED" -> bsm.setArchived(false);

            case "LOCKED" -> bsm.setLockForComments(true);

            case "UNLOCKED" -> bsm.setLockForComments(false);
        }
        this.blogRepository.saveAndFlush(this.modelMapper.map(bsm,Blog.class));
    }

    @Override
    public void addBlogComment(String blogTitle, String blogCommentTitle) {

        BlogServiceModel bsm = this.findByTitle(blogTitle);
        if (bsm != null){
            bsm.getComments().add(this.blogCommentService.findByTitle(blogCommentTitle));
            this.blogRepository.saveAndFlush(this.modelMapper.map(bsm,Blog.class));
        }

    }

}
