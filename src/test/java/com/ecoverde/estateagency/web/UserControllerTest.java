package com.ecoverde.estateagency.web;
import com.ecoverde.estateagency.model.entity.User;
import com.ecoverde.estateagency.model.service.UserServiceModel;
import com.ecoverde.estateagency.repositories.UserRepository;
import com.ecoverde.estateagency.service.RoleService;
import com.ecoverde.estateagency.service.UserService;
import net.bytebuddy.asm.Advice;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private Model model;
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

    }
    @Test
    public void testRegisterView() throws Exception {
        mvc.perform(get("/users/register"))
                .andExpect(view().name("register"))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegisterUserCorrectly() throws Exception {
        mvc.perform(post("/users/register")
                .param("username","parti1")
                .param("password","parti2")
                .param("confirmPassword","parti2")
                .param("phoneNumber","0893812345")
                .param("firstName","Pencho")
                .param("lastName","Penev"))
        .andExpect(view().name("redirect:/login"));

     Assert.assertEquals("parti1",this.userService.findByUsername("parti1").getUsername());

    }

    @Test
    public void testRegisterUserInsertedCorrectlyInDB() throws Exception {
        mvc.perform(post("/users/register")
                .param("username","parti2")
                .param("password","parti2")
                .param("confirmPassword","parti2")
                .param("phoneNumber","0893872345")
                .param("firstName","Peter")
                .param("lastName","Penchev"))
                .andExpect(view().name("redirect:/login"));

        UserServiceModel user = this.userService.findByUsername("parti2");
        Assert.assertNotNull(user);
        Assert.assertEquals("Peter",user.getFirstName());

    }

    @Test
    @WithMockUser("spring")
    public void testRegisterUserWithNotValidParameters() throws Exception {
        mvc.perform(post("/users/register")
                .param("username","par")
                .param("password","parti2")
                .param("confirmPassword","parti")
                .param("phoneNumber","08938123")
                .param("firstName","Pe")
                .param("lastName","Pev"))
                .andExpect(view().name("redirect:register"));

        Assert.assertNull(this.userService.findByUsername("par"));

    }

    @Test
    @WithMockUser("gosho")
    public void testUserProfileViewAndModel() throws Exception {
        Principal principalMocked =  Mockito.mock(Principal.class);
        Mockito.when(principalMocked.getName()).thenReturn("pesho");
        mvc.perform(get("/users/profile")
                .principal(principalMocked))
                .andExpect(model().attributeExists("userProfile"))
                .andExpect(view().name("user-profile"));

    }


    @Test
    @WithMockUser("gosho")
    public void testUserEditViewAndModel() throws Exception {
        Principal principalMocked =  Mockito.mock(Principal.class);
        Mockito.when(principalMocked.getName()).thenReturn("pesho");
        mvc.perform(get("/users/edit")
                .principal(principalMocked))
                .andExpect(model().attributeExists("userEditBindingModel"))
                .andExpect(view().name("edit-profile"));

    }

    @Test
    @WithMockUser("gosho")
    public void testUserEditProfileWrongPasswordException() throws Exception {
        Principal principalMocked =  Mockito.mock(Principal.class);
        Mockito.when(principalMocked.getName()).thenReturn("pesho");
        mvc.perform(post("/users/edit")
                .param("username","pesho")
                .param("password","556677")
                .param("passwordNew","55897")
                .param("confirmNewPassword","55897"))
                .andExpect(flash().attributeExists("wrongPassword"))
                .andExpect(view().name("redirect:edit"));

    }
}
