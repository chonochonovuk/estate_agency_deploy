package com.ecoverde.estateagency.web;

import com.ecoverde.estateagency.model.binding.BlogAddBindingModel;
import com.ecoverde.estateagency.model.entity.Blog;
import com.ecoverde.estateagency.model.entity.BlogComment;
import com.ecoverde.estateagency.model.entity.User;
import com.ecoverde.estateagency.model.service.BlogCommentServiceModel;
import com.ecoverde.estateagency.model.service.BlogServiceModel;
import com.ecoverde.estateagency.model.service.UserServiceModel;
import com.ecoverde.estateagency.repositories.BlogRepository;
import com.ecoverde.estateagency.service.BlogCommentService;
import com.ecoverde.estateagency.service.BlogService;
import com.ecoverde.estateagency.service.RoleService;
import com.ecoverde.estateagency.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BlogServiceTest {

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private BlogCommentService blogCommentService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

    @Before
    public void dataInit(){
        this.roleService.addRole("ROLE_USER");
        UserServiceModel user = new UserServiceModel();
        user.setUsername("pesho");
        user.setPassword("pesho2");
        user.setFirstName("Pesho");
        user.setLastName("Radinov");
        user.setPhoneNumber("0884885077");
        this.userService.register(user);

        Blog post1 = new Blog();
        User author = this.modelMapper.map(this.userService.findByUsername("pesho"),User.class);
        post1.setAuthor(author);
        post1.setTitle("Living In Bulgaria, A Village Life");
        post1.setContent("My name is Joanna. I came to Bulgaria 6 years ago, fell in love with the people and the country with all its traditions, bought a home, closed my ethnic shop in UK and moved here permanently 3 years ago. I am married and I have 3 children, 3 step children and 6 grand children.");
        post1.setCreatedAt(LocalDate.parse("2020-01-12", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        BlogCommentServiceModel bc2 = new BlogCommentServiceModel();
        bc2.setAuthor(this.userService.findByUsername("pesho"));
        bc2.setTitle("England is glorious");
        bc2.setContent("Apart from being with family & the fact England is glorious in the sunshine the other great thing about being here, at this time, is the Queens Diamond  ");
        bc2.setPublishedAt(LocalDateTime.parse("2020-02-11T12:34", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
        this.blogCommentService.addBlogComment(bc2);
        Set<BlogComment> comments1 = new HashSet<>();
        comments1.add(this.modelMapper.map(this.blogCommentService.findByTitle("England is glorious"),BlogComment.class));
        post1.setComments(comments1);
        post1.setArchived(false);
        post1.setLockForComments(false);
        this.blogRepository.saveAndFlush(post1);
    }

    @After
    public void cleanUp(){
        this.blogRepository.deleteAll();
    }

    @Test
    public void testFindByTitle(){
        Blog blog = this.blogRepository.findByTitle("Living In Bulgaria, A Village Life").orElse(null);

        Assert.assertNotNull(blog);

        Assert.assertEquals(1,blog.getComments().size());

        Assert.assertEquals("pesho",blog.getAuthor().getUsername());
    }

    @Test
    public void testAddComment(){
        BlogCommentServiceModel blogC = new BlogCommentServiceModel();
        blogC.setAuthor(this.userService.findByUsername("pesho"));
        blogC.setPublishedAt(LocalDateTime.now());
        blogC.setTitle("Airport One");
        blogC.setContent("Once at the airport, before then");
        this.blogCommentService.addBlogComment(blogC);
        this.blogService.addBlogComment("Living In Bulgaria, A Village Life","Airport One");
        BlogServiceModel blogServiceModel = this.blogService.findByTitle("Living In Bulgaria, A Village Life");
        Assert.assertEquals(2,blogServiceModel.getComments().size());
    }
}
