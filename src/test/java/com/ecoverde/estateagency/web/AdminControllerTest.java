package com.ecoverde.estateagency.web;

import com.ecoverde.estateagency.model.entity.Role;
import com.ecoverde.estateagency.model.service.RoleServiceModel;
import com.ecoverde.estateagency.model.service.UserServiceModel;
import com.ecoverde.estateagency.service.RoleService;
import com.ecoverde.estateagency.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class AdminControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ModelMapper modelMapper;

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

    }

    @Test
    @WithMockUser(roles = "ADMIN",username = "admin")
    public void testAdminViewAndModel() throws Exception {
        mvc
                .perform(get("/admin/manage-users"))
                .andExpect(model().attributeExists("allUsers"))
                .andExpect(view().name("admin"));
    }

    @Test
    @WithMockUser(roles = "ADMIN",username = "admin")
    public void testAdminChangeUserRoleToModerator() throws Exception {
        mvc
                .perform(post("/admin//set-role-moderator/" + "pesho"))
                .andExpect(view().name("redirect:/admin/manage-users"));

        RoleServiceModel rsm = this.roleService.findByAuthority("ROLE_MODERATOR");


        UserServiceModel usm = this.userService.findByUsername("pesho");
        for (RoleServiceModel r: usm.getAuthorities()) {
            Assert.assertEquals(r.getAuthority(),rsm.getAuthority());
        }
    }

    @Test
    @WithMockUser(roles = "ADMIN",username = "admin")
    public void testAdminDisableUser() throws Exception {
        mvc
                .perform(post("/admin//change-status-disabled/" + "pesho"))
                .andExpect(view().name("redirect:/admin/manage-users"));


        UserServiceModel usm = this.userService.findByUsername("pesho");
        Assert.assertFalse(usm.isEnabled());
    }

}
