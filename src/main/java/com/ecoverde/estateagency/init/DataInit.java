package com.ecoverde.estateagency.init;

import com.ecoverde.estateagency.service.BlogService;
import com.ecoverde.estateagency.service.PropertyService;
import com.ecoverde.estateagency.service.RoleService;
import com.ecoverde.estateagency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class DataInit implements CommandLineRunner {
    private final UserService userService;
    private final RoleService roleService;
    private final PropertyService propertyService;
    private final BlogService blogService;

    @Autowired
    public DataInit(UserService userService, RoleService roleService, PropertyService propertyService, BlogService blogService) {
        this.userService = userService;
        this.roleService = roleService;
        this.propertyService = propertyService;
        this.blogService = blogService;
    }

    @Override
    public void run(String... args) throws Exception {
           this.roleService.rolesInit();
           this.userService.usersInit();
           this.propertyService.propertiesInit();
           this.blogService.blogsInit();
    }
}
