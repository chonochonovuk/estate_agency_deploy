package com.ecoverde.estateagency.web;


import com.ecoverde.estateagency.model.binding.BlogCommentBindingModel;
import com.ecoverde.estateagency.model.entity.BlogComment;
import com.ecoverde.estateagency.model.service.BlogCommentServiceModel;
import com.ecoverde.estateagency.model.service.BlogServiceModel;
import com.ecoverde.estateagency.service.BlogCommentService;
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
import java.time.LocalDateTime;
import java.util.Set;

@Controller
@RequestMapping("/blog")
public class BlogController {
    private final BlogService blogService;
    private final BlogCommentService blogCommentService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public BlogController(BlogService blogService, BlogCommentService blogCommentService, UserService userService, ModelMapper modelMapper) {
        this.blogService = blogService;
        this.blogCommentService = blogCommentService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String blog(Model model){
        Set<BlogServiceModel> blogs = this.blogService.findAll();
        model.addAttribute("allBlogs",blogs);
        return "blog";
    }
    @GetMapping("/details/{title}")
    public String blogDetails(@PathVariable String title, Model model){
            model.addAttribute("blogDetails",this.blogService.findByTitle(title));
        if (!model.containsAttribute("blogCommentBindingModel")){
            model.addAttribute("blogCommentBindingModel",new BlogCommentBindingModel());
        }
        return "blog-single";
    }

    @PostMapping("/lock-for-comments/{title}")
    public ModelAndView lockBlogForComments(@PathVariable String title, ModelAndView modelAndView){
        this.blogService.changeBlogStatus(title,"LOCKED");
        modelAndView.addObject("title",title);
        modelAndView.setViewName("redirect:/blog/details/{title}");
        return modelAndView;
    }

    @PostMapping("/unlock-for-comments/{title}")
    public ModelAndView unlockBlogForComments(@PathVariable String title, ModelAndView modelAndView){
        this.blogService.changeBlogStatus(title,"UNLOCKED");
        modelAndView.addObject("title",title);
        modelAndView.setViewName("redirect:/blog/details/{title}");
        return modelAndView;
    }



    @PostMapping("/add-comment")
    public ModelAndView addBlogCommentConfirm(@RequestParam("title") String title,
                                       @Valid @ModelAttribute("blogCommentBindingModel") BlogCommentBindingModel blogCommentBindingModel,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes,
                                       Principal principal,
                                       ModelAndView modelAndView){
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("blogCommentBindingModel",blogCommentBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.blogCommentBindingModel",bindingResult);
            modelAndView.addObject("title",title);
            modelAndView.setViewName("redirect:/blog/details/{title}");
            return modelAndView;
        }

        if (this.blogCommentService.findByTitle(blogCommentBindingModel.getCommentTitle()) != null){
            bindingResult.rejectValue("commentTitle","error.commentTitle","This title already exist!");
            redirectAttributes.addFlashAttribute("blogCommentBindingModel",blogCommentBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.blogCommentBindingModel",bindingResult);
            modelAndView.addObject("title",title);
            modelAndView.setViewName("redirect:/blog/details/{title}");
            return modelAndView;
        }

        blogCommentBindingModel.setAuthor(this.userService.findByUsername(principal.getName()));
        blogCommentBindingModel.setPublishedAt(LocalDateTime.now());
        BlogCommentServiceModel bcsm = this.modelMapper.map(blogCommentBindingModel, BlogCommentServiceModel.class);
        this.blogCommentService.addBlogComment(bcsm);
        this.blogService.addBlogComment(title,blogCommentBindingModel.getCommentTitle());
        modelAndView.addObject("title",title);
        modelAndView.setViewName("redirect:/blog/details/{title}");
        return modelAndView;
    }

}
