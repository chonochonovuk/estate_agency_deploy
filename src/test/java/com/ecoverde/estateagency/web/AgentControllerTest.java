package com.ecoverde.estateagency.web;

import com.ecoverde.estateagency.model.entity.Image;
import com.ecoverde.estateagency.model.service.*;
import com.ecoverde.estateagency.service.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AgentControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

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
    private WebApplicationContext webApplicationContext;


    @Before()
    public void setup()
    {
        //Init MockMvc Object and build
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        MockitoAnnotations.initMocks(this);

        this.roleService.addRole("ROLE_USER");
        this.roleService.addRole("ROLE_MODERATOR");

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

    }

    @Test
    @WithMockUser(roles = "AGENT",username = "agent1")
    public void testAgentModelAndView() throws Exception {
        mvc
                .perform(get("/property-add"))
                .andExpect(model().attributeExists("propertyAddBindingModel"))
                .andExpect(view().name("property-new"));
    }


    @Test
    @WithMockUser(roles = "AGENT",username = "agent1")
    public void testAgentAddNewProperty() throws Exception {
        mvc
                .perform(post("/property-add")
                .param("username","pesho")
                .param("propertyName","Vilna Zona")
                .param("propertyType","Villa")
                .param("town","Melnik")
                .param("area","Center")
                .param("fullAddress","16 Vladimir Vazov Street")
                .param("size","165")
                .param("rooms","5")
                .param("bathrooms","3")
                .param("year","2006")
                .param("price","160000")
                .param("description","Nice house in one of Bulgaria smallest town with cosmopolitan view " +
                        "and good neighbours")

                )
                .andExpect(view().name("redirect:/property"));

        Assert.assertNotNull(this.propertyService.findByPropertyName("Vilna Zona"));
    }

}
