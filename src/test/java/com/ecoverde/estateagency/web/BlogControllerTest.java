package com.ecoverde.estateagency.web;


import com.ecoverde.estateagency.model.entity.Blog;
import com.ecoverde.estateagency.model.entity.BlogComment;
import com.ecoverde.estateagency.model.entity.User;
import com.ecoverde.estateagency.model.service.BlogCommentServiceModel;
import com.ecoverde.estateagency.model.service.BlogServiceModel;
import com.ecoverde.estateagency.model.service.UserServiceModel;
import com.ecoverde.estateagency.service.BlogCommentService;
import com.ecoverde.estateagency.service.BlogService;
import com.ecoverde.estateagency.service.RoleService;
import com.ecoverde.estateagency.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class BlogControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BlogService blogService;

    @Autowired
    private BlogCommentService blogCommentService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private Model model;

    private final String TITLE = "Living In Bulgaria, A Village Life";

    @Before()
    public void setup()
    {
        //Init MockMvc Object and build
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MockitoAnnotations.initMocks(this);

        this.roleService.addRole("ROLE_USER");

        UserServiceModel user = new UserServiceModel();
        user.setUsername("pesho");
        user.setPassword("pesho2");
        user.setFirstName("Pesho");
        user.setLastName("Radinov");
        user.setPhoneNumber("0884885077");
        this.userService.register(user);

        BlogServiceModel post1 = new BlogServiceModel();
        UserServiceModel author = this.userService.findByUsername("pesho");
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
        Set<BlogCommentServiceModel> comments1 = new HashSet<>();
        comments1.add(this.blogCommentService.findByTitle("England is glorious"));
        post1.setComments(comments1);
        post1.setArchived(false);
        post1.setLockForComments(false);
        this.blogService.addBlog(post1);

    }

    @Test
    @WithMockUser("gosho")
    public void testBlogView() throws Exception {
        mvc.perform(get("/blog"))
                .andExpect(view().name("blog"));
    }

    @Test
    @WithMockUser("gosho")
    public void testBlogViewAndModel() throws Exception {
        mvc.perform(get("/blog"))
                .andExpect(model().attributeExists("allBlogs"))
                .andExpect(view().name("blog"));
    }

    @Test
    @WithMockUser("gosho")
    public void testBlogDetails() throws Exception {

        mvc.perform(get("/blog/details/" + TITLE)
        .param("title","Living In Bulgaria, A Village Life"))
                .andExpect(model().attributeExists("blogDetails"))
                .andExpect(view().name("blog-single"));
    }

    @Test
    @WithMockUser("gosho")
    public void testBlogLockForComments() throws Exception {

        mvc.perform(post("/blog/lock-for-comments/" + TITLE)
                .param("title","Living In Bulgaria, A Village Life"))
                .andExpect(model().attributeExists("title"))
                .andExpect(view().name("redirect:/blog/details/{title}"));

        Assert.assertTrue(this.blogService.findByTitle(TITLE).isLockForComments());
    }
    @Test
    @WithMockUser("gosho")
    public void testBlogAddComments() throws Exception {
        Principal mockedPrincipal = Mockito.mock(Principal.class);
        Mockito.when(mockedPrincipal.getName()).thenReturn("pesho");
        mvc.perform(post("/blog/add-comment")
                .param("title","Living In Bulgaria, A Village Life")
                .param("commentTitle","Three days in Sofia")
                .param("content","European Union Economic and Monetary Commissioner Pierre Moscovici commented that Bulgaria should strengthen its economy and improve control over certain institutions before the country decides")
                .principal(mockedPrincipal))
                .andExpect(model().attributeExists("title"))
                .andExpect(view().name("redirect:/blog/details/{title}"));
        Assert.assertEquals(2,this.blogService.findByTitle(TITLE).getComments().size());
    }

}
