package com.ecoverde.estateagency.web;

import com.ecoverde.estateagency.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/manage-users")
    @PreAuthorize("hasRole('ADMIN')")
    public String manageUsers(Model model){
        model.addAttribute("allUsers",this.userService.findAll());
        return "admin";
    }

    @PostMapping("/set-role-user/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView setRoleUser(@PathVariable String username, ModelAndView modelAndView){
        this.userService.userSetRole(username,"ROLE_USER");
        modelAndView.setViewName("redirect:/admin/manage-users");
        return modelAndView;
    }

    @PostMapping("/set-role-admin/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView setRoleAdmin(@PathVariable String username, ModelAndView modelAndView){
        this.userService.userSetRole(username,"ROLE_ADMIN");
        modelAndView.setViewName("redirect:/admin/manage-users");
        return modelAndView;
    }

    @PostMapping("/set-role-agent/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView setRoleAgent(@PathVariable String username, ModelAndView modelAndView){
        this.userService.userSetRole(username,"ROLE_AGENT");
        modelAndView.setViewName("redirect:/admin/manage-users");
        return modelAndView;
    }

    @PostMapping("/set-role-moderator/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView setRoleModerator(@PathVariable String username, ModelAndView modelAndView){
        this.userService.userSetRole(username,"ROLE_MODERATOR");
        modelAndView.setViewName("redirect:/admin/manage-users");
        return modelAndView;
    }

    @PostMapping("/set-role-owner/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView setRoleOwner(@PathVariable String username, ModelAndView modelAndView){
        this.userService.userSetRole(username,"ROLE_OWNER");
        modelAndView.setViewName("redirect:/admin/manage-users");
        return modelAndView;
    }

    @PostMapping("/change-status-enabled/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView changeUserStatusEnabled(@PathVariable String username, ModelAndView modelAndView){
        this.userService.changeUserStatus(username,"ENABLED");
        modelAndView.setViewName("redirect:/admin/manage-users");
        return modelAndView;
    }

    @PostMapping("/change-status-disabled/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView changeUserStatusDisabled(@PathVariable String username, ModelAndView modelAndView){
        this.userService.changeUserStatus(username,"DISABLED");
        modelAndView.setViewName("redirect:/admin/manage-users");
        return modelAndView;
    }
}
