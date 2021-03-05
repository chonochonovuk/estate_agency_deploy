package com.ecoverde.estateagency.service.impl;

import com.ecoverde.estateagency.model.entity.Role;
import com.ecoverde.estateagency.model.service.RoleServiceModel;
import com.ecoverde.estateagency.repositories.RoleRepository;
import com.ecoverde.estateagency.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public RoleServiceModel findByAuthority(String authority) {
        return this.roleRepository.findByAuthority(authority)
                .map(role -> this.modelMapper.map(role,RoleServiceModel.class))
                .orElse(null);
    }

    @Override
    public void addRole(String authority) {
        if (this.roleRepository.findByAuthority(authority).isEmpty()){
            Role role = new Role();
            role.setAuthority(authority);
            this.roleRepository.saveAndFlush(role);
        }
    }

    @Override
    public void rolesInit(){
        if (this.roleRepository.count() == 0){
            Role root = new Role();
            root.setAuthority("ROLE_ROOT");
            Role admin = new Role();
            admin.setAuthority("ROLE_ADMIN");
            Role agent = new Role();
            agent.setAuthority("ROLE_AGENT");
            Role moderator = new Role();
            moderator.setAuthority("ROLE_MODERATOR");
            Role user = new Role();
            user.setAuthority("ROLE_USER");
            Role owner = new Role();
            owner.setAuthority("ROLE_OWNER");

            this.roleRepository.saveAndFlush(root);
            this.roleRepository.saveAndFlush(admin);
            this.roleRepository.saveAndFlush(agent);
            this.roleRepository.saveAndFlush(moderator);
            this.roleRepository.saveAndFlush(user);
            this.roleRepository.saveAndFlush(owner);


        }
    }

    @Override
    public Set<Role> findAll() {
        return this.roleRepository.findAll().stream().collect(Collectors.toUnmodifiableSet());
    }
}
