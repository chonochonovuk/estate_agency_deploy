package com.ecoverde.estateagency.web;

import com.ecoverde.estateagency.model.entity.Role;
import com.ecoverde.estateagency.repositories.RoleRepository;
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

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleServiceTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Before
    public void rolesInit(){
        Role user = new Role();
        user.setAuthority("ROLE_USER");

        Role admin = new Role();
        admin.setAuthority("ROLE_ADMIN");
        this.roleRepository.saveAndFlush(user);
        this.roleRepository.saveAndFlush(admin);
    }

    @After
    public void cleanRoles(){
        this.roleRepository.deleteAll();
    }

    @Test
    public void testFindByAuthority(){
        Role role = this.roleRepository.findByAuthority("ROLE_USER").orElse(null);
        Role role1 = this.roleRepository.findByAuthority("ROLE_ADMIN").orElse(null);

        Assert.assertNotNull(role);
        Assert.assertNotNull(role1);

        Assert.assertEquals("ROLE_USER",role.getAuthority());
        Assert.assertEquals("ROLE_ADMIN",role1.getAuthority());

    }
}
