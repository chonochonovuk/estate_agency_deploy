package com.ecoverde.estateagency.service;

import com.ecoverde.estateagency.model.entity.User;
import com.ecoverde.estateagency.model.service.UserServiceModel;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Set;

public interface UserService extends UserDetailsService {
    UserServiceModel register(UserServiceModel userServiceModel);

    UserServiceModel findByUsername(String username);

    Set<User> findAllEnabledFalseUsers();

    UserServiceModel editUserProfile(UserServiceModel userServiceModel ,String oldPassword);

    List<UserServiceModel> findAll();

    void userSetRole(String username,String authority);

    void changeUserStatus(String username, String status);

    void usersInit();

    void deleteAllNotEnabledUsers();

    void deleteUserByUsername(String username);
}
