package com.ecoverde.estateagency.service.impl;

import com.ecoverde.estateagency.model.entity.Role;
import com.ecoverde.estateagency.model.entity.User;
import com.ecoverde.estateagency.model.service.RoleServiceModel;
import com.ecoverde.estateagency.model.service.UserServiceModel;
import com.ecoverde.estateagency.repositories.UserRepository;
import com.ecoverde.estateagency.service.RoleService;
import com.ecoverde.estateagency.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!!!"));
        UserDetails userDetails = this.modelMapper.map(user,UserDetails.class);

        return userDetails;
    }

    @Override
    public UserServiceModel register(UserServiceModel userServiceModel) {
        User toRegister = this.modelMapper.map(userServiceModel,User.class);
        toRegister.setEnabled(true);
        toRegister.setCredentialsNonExpired(true);
        toRegister.setAccountNonLocked(true);
        toRegister.setAccountNonExpired(true);
        Set<Role> roles = new HashSet<>();
        roles.add(this.modelMapper.map(this.roleService.findByAuthority("ROLE_USER"),Role.class));
        toRegister.setAuthorities(roles);
        if (toRegister.getPassword() != null){
            toRegister.setPassword(passwordEncoder.encode(toRegister.getPassword()));
        }
        this.userRepository.saveAndFlush(toRegister);
        return this.modelMapper.map(toRegister,UserServiceModel.class);
    }

    @Override
    public UserServiceModel findByUsername(String username) {
        return this.userRepository.findByUsername(username).map(user -> this.modelMapper
        .map(user,UserServiceModel.class)).orElse(null);
    }

    @Override
    public Set<User> findAllEnabledFalseUsers() {
        return this.userRepository.findAllByEnabledFalse();
    }

    @Override
    public UserServiceModel editUserProfile(UserServiceModel userServiceModel, String oldPassword) {
        User user = this.userRepository.findByUsername(userServiceModel.getUsername())
                .orElse(null);
        if (!passwordEncoder.matches(oldPassword,user.getPassword())){
            throw new IllegalArgumentException("Incorrect Password!");
        }
        user.setPassword(userServiceModel.getPassword() != null ? this.passwordEncoder.encode(userServiceModel.getPassword()) : user.getPassword());
        this.userRepository.saveAndFlush(user);
        return this.modelMapper.map(user,UserServiceModel.class);
    }

    @Override
    public List<UserServiceModel> findAll() {
        return this.userRepository.findAll().stream()
                .filter(user -> user.getAuthorities().size() < 6)
                .map(user -> this.modelMapper.map(user,UserServiceModel.class))
                .collect(Collectors.toList());
    }

    @Override
    public void userSetRole(String username, String authority) {
       UserServiceModel usm = this.findByUsername(username);
       usm.getAuthorities().clear();
        switch (authority) {
            case "ROLE_USER" -> usm.getAuthorities().add(this.roleService.findByAuthority("ROLE_USER"));
            case "ROLE_OWNER" -> {
                usm.getAuthorities().add(this.roleService.findByAuthority("ROLE_OWNER"));
            }
            case "ROLE_AGENT" -> {
                usm.getAuthorities().add(this.roleService.findByAuthority("ROLE_AGENT"));
            }
            case "ROLE_MODERATOR" -> {
                usm.getAuthorities().add(this.roleService.findByAuthority("ROLE_MODERATOR"));
            }
            case "ROLE_ADMIN" -> {
                usm.getAuthorities().add(this.roleService.findByAuthority("ROLE_ADMIN"));
            }
        }
       this.userRepository.saveAndFlush(this.modelMapper.map(usm,User.class));
    }

    @Override
    public void changeUserStatus(String username, String status) {
        UserServiceModel usm = this.findByUsername(username);
        switch (status) {
            case "ENABLED" -> {
                usm.setEnabled(true);
                usm.setAccountNonExpired(true);
                usm.setAccountNonLocked(true);
                usm.setCredentialsNonExpired(true);
            }
            case "DISABLED" -> usm.setEnabled(false);
        }
        this.userRepository.saveAndFlush(this.modelMapper.map(usm,User.class));
    }

    @Transactional
    @Override
    public void deleteUserByUsername(String username) {
        if (this.findByUsername(username) != null){
            this.userRepository.deleteByUsername(username);
        }
    }

    @Transactional
    @Override
    public void deleteAllNotEnabledUsers() {
         if (this.findAllEnabledFalseUsers() != null){
             Set<User> notEnabled = this.findAllEnabledFalseUsers();
             for (User u: notEnabled) {
                 this.deleteUserByUsername(u.getUsername());
             }
         }
    }

    @Override
    public void usersInit() {
       if (this.userRepository.count() == 0){
           User root = new User();
           root.setUsername("Superuser");
           root.setPassword(this.passwordEncoder.encode("1234"));
           Set<Role> roles = this.roleService.findAll();
           root.setEnabled(true);
           root.setAuthorities(roles);
           root.setAccountNonExpired(true);
           root.setAccountNonLocked(true);
           root.setCredentialsNonExpired(true);
           root.setFirstName("Chono");
           root.setLastName("Chonov");
           root.setPhoneNumber("0895325067");


           User agent = new User();
           agent.setUsername("agent1");
           agent.setPassword(this.passwordEncoder.encode("agent"));
           Set<Role> agentRole = new HashSet<>();
           agentRole.add(this.modelMapper.map(this.roleService.findByAuthority("ROLE_AGENT"),Role.class));
           agent.setAuthorities(agentRole);
           agent.setEnabled(true);
           agent.setAccountNonExpired(true);
           agent.setAccountNonLocked(true);
           agent.setCredentialsNonExpired(true);
           agent.setFirstName("Alice");
           agent.setLastName("Zenova");
           agent.setPhoneNumber("0894325077");

           User user = new User();
           user.setUsername("pesho");
           user.setPassword(this.passwordEncoder.encode("pesho2"));
           Set<Role> userRole = new HashSet<>();
           userRole.add(this.modelMapper.map(this.roleService.findByAuthority("ROLE_USER"),Role.class));
           user.setAuthorities(userRole);
           user.setEnabled(true);
           user.setAccountNonExpired(true);
           user.setAccountNonLocked(true);
           user.setCredentialsNonExpired(true);
           user.setFirstName("Pesho");
           user.setLastName("Radinov");
           user.setPhoneNumber("0884885077");

           User owner1 = new User();
           owner1.setUsername("ivan56");
           owner1.setPassword(this.passwordEncoder.encode("ivan65"));
           Set<Role> owner1Role = new HashSet<>();
           owner1Role.add(this.modelMapper.map(this.roleService.findByAuthority("ROLE_OWNER"),Role.class));
           owner1.setAuthorities(owner1Role);
           owner1.setEnabled(true);
           owner1.setAccountNonExpired(true);
           owner1.setAccountNonLocked(true);
           owner1.setCredentialsNonExpired(true);
           owner1.setFirstName("Ivan");
           owner1.setLastName("Vitov");
           owner1.setPhoneNumber("0898164456");

           User owner2 = new User();
           owner2.setUsername("jana66");
           owner2.setPassword(this.passwordEncoder.encode("jana66"));
           owner2.setAuthorities(owner1Role);
           owner2.setEnabled(true);
           owner2.setAccountNonExpired(true);
           owner2.setAccountNonLocked(true);
           owner2.setCredentialsNonExpired(true);
           owner2.setFirstName("Jana");
           owner2.setLastName("Jenova");
           owner2.setPhoneNumber("0887625066");

           User moderator = new User();
           moderator.setUsername("joanna");
           moderator.setPassword(this.passwordEncoder.encode("ljoanna"));
           Set<Role> moderatorRole = new HashSet<>();
           moderatorRole.add(this.modelMapper.map(this.roleService.findByAuthority("ROLE_MODERATOR"),Role.class));
           moderator.setAuthorities(moderatorRole);
           moderator.setEnabled(true);
           moderator.setAccountNonExpired(true);
           moderator.setAccountNonLocked(true);
           moderator.setCredentialsNonExpired(true);
           moderator.setFirstName("Joanna");
           moderator.setLastName("Ignasova");
           moderator.setPhoneNumber("0888625066");


           this.userRepository.saveAndFlush(root);
           this.userRepository.saveAndFlush(agent);
           this.userRepository.saveAndFlush(user);
           this.userRepository.saveAndFlush(owner1);
           this.userRepository.saveAndFlush(owner2);
           this.userRepository.saveAndFlush(moderator);

       }

    }



}
