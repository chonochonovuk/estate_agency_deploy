package com.ecoverde.estateagency.web;

import com.ecoverde.estateagency.model.binding.BlogAddBindingModel;
import com.ecoverde.estateagency.model.service.BlogServiceModel;
import com.ecoverde.estateagency.service.BlogService;
import com.ecoverde.estateagency.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;

@Controller
@RequestMapping("/blog-add")
public class BlogAddController {
    private final BlogService blogService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public BlogAddController(BlogService blogService, UserService userService, ModelMapper modelMapper) {
        this.blogService = blogService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String addNewBlog(Model model){
        if (!model.containsAttribute("blogAddBindingModel")){
            model.addAttribute("blogAddBindingModel",new BlogAddBindingModel());
        }
        return "blog-new";
    }

    @PostMapping
    public ModelAndView addNewBlogConfirm(@Valid @ModelAttribute("blogAddBindingModel") BlogAddBindingModel blogAddBindingModel,
                                          BindingResult bindingResult,
                                          Principal principal,
                                          RedirectAttributes re,
                                          ModelAndView modelAndView){
        if (bindingResult.hasErrors()){
            re.addFlashAttribute("blogAddBindingModel",blogAddBindingModel);
            re.addFlashAttribute("org.springframework.validation.BindingResult.blogAddBindingModel",bindingResult);
            modelAndView.setViewName("redirect:blog-add");
            return modelAndView;
        }

        if (this.blogService.findByTitle(blogAddBindingModel.getTitle()) != null){
            bindingResult.rejectValue("title","error.title","This title already exist!");
            re.addFlashAttribute("blogAddBindingModel",blogAddBindingModel);
            re.addFlashAttribute("org.springframework.validation.BindingResult.blogAddBindingModel",bindingResult);
            modelAndView.setViewName("redirect:blog-add");
            return modelAndView;
        }
        blogAddBindingModel.setAuthor(this.userService.findByUsername(principal.getName()));
        blogAddBindingModel.setCreatedAt(LocalDate.now());
        this.blogService.addBlog(this.modelMapper.map(blogAddBindingModel, BlogServiceModel.class));

        modelAndView.setViewName("redirect:/blog");
        return modelAndView;
    }
}
