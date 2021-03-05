package com.ecoverde.estateagency.web;

import com.ecoverde.estateagency.model.binding.UserEditBindingModel;
import com.ecoverde.estateagency.model.binding.UserRegisterBindingModel;
import com.ecoverde.estateagency.model.service.UserServiceModel;
import com.ecoverde.estateagency.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/register")
    public String register(Model model){
        if (!model.containsAttribute("userRegisterBindingModel")){
            model.addAttribute("userRegisterBindingModel",new UserRegisterBindingModel());
        }

        return "register";
    }


    @PostMapping("/register")
    public ModelAndView registerConfirm(@Valid @ModelAttribute("userRegisterBindingModel") UserRegisterBindingModel userRegisterBindingModel,
                                        BindingResult bindingResult,
                                        ModelAndView modelAndView,
                                        RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors() || !userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())){
            redirectAttributes.addFlashAttribute("userRegisterBindingModel",userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel",bindingResult);
            modelAndView.setViewName("redirect:register");
            return modelAndView;
        }

        if (this.userService.findByUsername(userRegisterBindingModel.getUsername()) != null){
            bindingResult.rejectValue("username","error.username","This username already exist!");
            redirectAttributes.addFlashAttribute("userRegisterBindingModel",userRegisterBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegisterBindingModel",bindingResult);
            modelAndView.setViewName("redirect:register");
            return modelAndView;
        }

        this.userService.register(this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class));
        modelAndView.setViewName("redirect:/login");
        return modelAndView;

    }

    @GetMapping("/profile")
    public String profile(Principal principal,Model model){
        model.addAttribute("userProfile",this.userService.findByUsername(principal.getName()));
        return "user-profile";
    }

    @GetMapping("/edit")
    public String editProfile(Principal principal,Model model){
        if (!model.containsAttribute("userEditBindingModel")){
            UserEditBindingModel userEditBindingModel = this.modelMapper.map(this.userService.
                    findByUsername(principal.getName()), UserEditBindingModel.class);
            model.addAttribute("userEditBindingModel",userEditBindingModel);
        }
        return "edit-profile";
    }

    @PostMapping("/edit")
    public ModelAndView editProfileConfirm(@Valid @ModelAttribute("userEditBindingModel") UserEditBindingModel userEditBindingModel,
                                           BindingResult bindingResult,
                                           ModelAndView modelAndView,
                                           RedirectAttributes redirectAttributes){

        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("userEditBindingModel",userEditBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userEditBindingModel",bindingResult);
            modelAndView.setViewName("redirect:edit");
            return modelAndView;
        }

        this.userService.
                editUserProfile(this.userService.findByUsername(userEditBindingModel.getUsername()), userEditBindingModel.getPassword());
        modelAndView.setViewName("redirect:profile");
        return modelAndView;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String exceptionWrongPassword(IllegalArgumentException ie,RedirectAttributes re){
        re.addFlashAttribute("wrongPassword",ie.getMessage());
        return "redirect:edit";
    }

}
