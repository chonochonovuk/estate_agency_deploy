package com.ecoverde.estateagency.service;

import com.ecoverde.estateagency.model.entity.Role;
import com.ecoverde.estateagency.model.service.RoleServiceModel;

import java.util.Set;

public interface RoleService {
    RoleServiceModel findByAuthority(String authority);

    void addRole(String authority);

    void rolesInit();

    Set<Role> findAll();
}
