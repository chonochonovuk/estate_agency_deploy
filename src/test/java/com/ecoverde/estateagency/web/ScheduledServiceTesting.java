package com.ecoverde.estateagency.web;

import com.ecoverde.estateagency.model.entity.Blog;
import com.ecoverde.estateagency.model.entity.BlogComment;
import com.ecoverde.estateagency.model.entity.Image;
import com.ecoverde.estateagency.model.entity.User;
import com.ecoverde.estateagency.model.service.*;
import com.ecoverde.estateagency.repositories.BlogRepository;
import com.ecoverde.estateagency.scheduling.ScheduledRepositoryCleaner;
import com.ecoverde.estateagency.service.*;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ScheduledServiceTesting {

    @Autowired
    private ScheduledRepositoryCleaner scheduledRepositoryCleaner;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private BlogCommentService blogCommentService;
    @Autowired
    private RoleService roleService;

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PropertyTypeService propertyTypeService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private TownService townService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Before
    public void dataInit() {
        this.roleService.addRole("ROLE_USER");
        UserServiceModel user = new UserServiceModel();
        user.setUsername("pesho");
        user.setPassword("pesho2");
        user.setFirstName("Pesho");
        user.setLastName("Radinov");
        user.setPhoneNumber("0884885077");
        this.userService.register(user);

        TownServiceModel t2 = new TownServiceModel();
        t2.setName("Burgas");
        this.townService.addTown(t2);
        AddressServiceModel addr2 = new AddressServiceModel();
        addr2.setArea("Sveti Vlas");
        addr2.setFullAddress("13 Pirin street");
        this.addressService.addAddress(addr2);
        PropertyTypeServiceModel pt2 = new PropertyTypeServiceModel();
        pt2.setTypeName("House");
        this.propertyTypeService.addPropertyType(pt2);
        PropertyServiceModel prop2 = new PropertyServiceModel();
        prop2.setPropertyName("Sveti Vlas Pirin");
        prop2.setPropertyTypeServiceModel(pt2);
        prop2.setTownServiceModel(t2);
        prop2.setAddressServiceModel(addr2);
        prop2.setDescription("This is a beautiful and spacious three bedrooms house with spectacular " +
                "sea views across the bay to the ancient town of Nessebar." +
                " It is situated in the beautiful and salubrious 'Garden of Eden'," +
                " in the upmarket town of St Vlas.");
        prop2.setPrice(new BigDecimal(125000));
        prop2.setBathrooms(2);
        prop2.setRooms(5);
        prop2.setDate(LocalDate.parse("2020-07-17", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        prop2.setYear(2006);
        prop2.setSize(242);
        Image img3 = new Image();
        img3.setUrl("/images/burgas/stVlas/house1.jpg");
        prop2.setPhotos(img3);
        prop2.setOwner(this.userService.findByUsername("pesho"));
        this.propertyService.addProperty(prop2);

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

    @Test
    public void testPropertiesUsersAndBlogs(){
        PropertyServiceModel propertyServiceModel = this.propertyService.findByPropertyName("Sveti Vlas Pirin");
        UserServiceModel user = this.userService.findByUsername("pesho");
        Set<Blog> blog = this.blogRepository.findAllByAuthorUsername("pesho");

        Assert.assertEquals(1,blog.size());
        Assert.assertNotNull(user);
        Assert.assertNotNull(propertyServiceModel);

    }

    @Test
    public void testScheduledNotCleaningEnabledUsers(){
        PropertyServiceModel propertyServiceModel = this.propertyService.findByPropertyName("Sveti Vlas Pirin");
        UserServiceModel user = this.userService.findByUsername("pesho");
        Set<Blog> blog = this.blogRepository.findAllByAuthorUsername("pesho");

        Assert.assertEquals(1,blog.size());
        Assert.assertNotNull(user);
        Assert.assertNotNull(propertyServiceModel);

        this.scheduledRepositoryCleaner.cleanUpRepositories();

        Assert.assertEquals(1,blog.size());
        Assert.assertNotNull(user);
        Assert.assertNotNull(propertyServiceModel);



    }

    @Test
    public void testScheduledTaskWorkCorrectly(){
        PropertyServiceModel propertyServiceModel = this.propertyService.findByPropertyName("Sveti Vlas Pirin");
        UserServiceModel user = this.userService.findByUsername("pesho");
        Set<Blog> blog = this.blogRepository.findAllByAuthorUsername("pesho");

        Assert.assertEquals(1,blog.size());
        Assert.assertNotNull(user);
        Assert.assertNotNull(propertyServiceModel);

        this.userService.changeUserStatus(user.getUsername(),"DISABLED");


        this.scheduledRepositoryCleaner.cleanUpRepositories();

        PropertyServiceModel propertyServiceModel1 = this.propertyService.findByPropertyName("Sveti Vlas Pirin");
        UserServiceModel user1 = this.userService.findByUsername("pesho");
        Set<Blog> blog1 = this.blogRepository.findAllByAuthorUsername("pesho");

        Assert.assertEquals(0,blog1.size());
        Assert.assertNull(user1);
        Assert.assertNull(propertyServiceModel1);



    }
}
