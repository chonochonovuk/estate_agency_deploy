package com.ecoverde.estateagency.scheduling;

import com.ecoverde.estateagency.model.entity.User;
import com.ecoverde.estateagency.service.BlogCommentService;
import com.ecoverde.estateagency.service.BlogService;
import com.ecoverde.estateagency.service.PropertyService;
import com.ecoverde.estateagency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class ScheduledRepositoryCleaner {
    private final UserService userService;
    private final PropertyService propertyService;
    private final BlogService blogService;
    private final BlogCommentService blogCommentService;


    @Autowired
    public ScheduledRepositoryCleaner(UserService userService, PropertyService propertyService, BlogService blogService, BlogCommentService blogCommentService) {
        this.userService = userService;
        this.propertyService = propertyService;
        this.blogService = blogService;
        this.blogCommentService = blogCommentService;
    }

    @Scheduled(cron = "0 0 2 ? * SUN")
    public void cleanUpRepositories(){
        if (this.userService.findAllEnabledFalseUsers() != null){
            Set<User> toCleanRepos = this.userService.findAllEnabledFalseUsers();
            for (User user : toCleanRepos) {
                this.propertyService.deletePropertiesByOwnerUsername(user.getUsername());
                this.blogService.deleteAllByAuthorUsername(user.getUsername());
                this.blogCommentService.deleteAllByAuthorUsername(user.getUsername());
            }
            this.userService.deleteAllNotEnabledUsers();
        }

    }
}
