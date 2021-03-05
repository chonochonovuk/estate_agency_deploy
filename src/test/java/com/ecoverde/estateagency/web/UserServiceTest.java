package com.ecoverde.estateagency.web;

import com.ecoverde.estateagency.model.entity.Role;
import com.ecoverde.estateagency.model.entity.User;
import com.ecoverde.estateagency.model.service.UserServiceModel;
import com.ecoverde.estateagency.repositories.UserRepository;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void init(){
        User user = new User();
        user.setPhoneNumber("0876543215");
        user.setUsername("stamat");
        user.setPassword(passwordEncoder.encode("2345"));
        user.setFirstName("Stamat");
        user.setLastName("Chushkov");
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setEnabled(true);
        this.roleService.addRole("ROLE_USER");
        user.setAuthorities(Set.of(this.modelMapper.map(this.roleService.findByAuthority("ROLE_USER"),Role.class)));
        User user2 = new User();
        user2.setPhoneNumber("0886543215");
        user2.setUsername("stoiko");
        user2.setPassword(passwordEncoder.encode("2545"));
        user2.setFirstName("Stoyko");
        user2.setLastName("Gushov");
        user2.setCredentialsNonExpired(true);
        user2.setAccountNonLocked(true);
        user2.setAccountNonExpired(true);
        user2.setEnabled(true);
        user2.setAuthorities(Set.of(this.modelMapper.map(this.roleService.findByAuthority("ROLE_USER"),Role.class)));
        this.userRepository.saveAndFlush(user);
        this.userRepository.saveAndFlush(user2);

    }

    @After
    public void clean(){
        this.userRepository.deleteAll();
    }

    @Test
    public void testFindUserByUsername(){
       User user = this.userRepository.findByUsername("stamat").orElse(null);
        User user2 = this.userRepository.findByUsername("stoiko").orElse(null);
       Assert.assertNotNull(user);
       Assert.assertTrue(passwordEncoder.matches("2345",user.getPassword()));
        Assert.assertNotNull(user2);
        Assert.assertTrue(passwordEncoder.matches("2545",user2.getPassword()));
    }

    @Test
    public void testLoadByUsername(){
        UserDetails userDetails = this.userService.loadUserByUsername("stamat");
        Assert.assertNotNull(userDetails);
    }

    @Test
    public void testRegisterUserServiceModel(){
        UserServiceModel user = new UserServiceModel();
        user.setUsername("pesho");
        user.setPassword("pesho2");
        user.setFirstName("Pesho");
        user.setLastName("Radinov");
        user.setPhoneNumber("0884885077");
        this.userService.register(user);
        UserDetails userDetails = this.userService.loadUserByUsername("pesho");
        Assert.assertNotNull(userDetails);
    }

    @Test
    public void testDeleteNotEnabledUser(){
        User user = this.userRepository.findByUsername("stamat").orElse(null);
        User user2 = this.userRepository.findByUsername("stoiko").orElse(null);
        if (user != null){
            this.userService.changeUserStatus(user.getUsername(),"DISABLED");
        }

        if (user2 != null){
            user2.setEnabled(false);
            this.userService.changeUserStatus(user2.getUsername(),"DISABLED");
        }
        this.userService.deleteAllNotEnabledUsers();
        Assert.assertEquals(0,this.userService.findAllEnabledFalseUsers().size());
        User userSt = this.userRepository.findByUsername("stamat").orElse(null);
        User userSi = this.userRepository.findByUsername("stoiko").orElse(null);
        Assert.assertNull(userSt);
        Assert.assertNull(userSi);
    }
}
