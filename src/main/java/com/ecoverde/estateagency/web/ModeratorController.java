package com.ecoverde.estateagency.web;

import com.ecoverde.estateagency.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/moderator")
public class ModeratorController {
    private final BlogService blogService;

    @Autowired
    public ModeratorController(BlogService blogService) {
        this.blogService = blogService;
    }


   @PostMapping("/blog-status-archived/{title}")
   @PreAuthorize("hasRole('MODERATOR')")
   public ModelAndView changeBlogStatusArchived(@PathVariable String title, ModelAndView modelAndView){
       this.blogService.changeBlogStatus(title,"ARCHIVED");
       modelAndView.setViewName("redirect:/blog");
       return modelAndView;
   }

    @PostMapping("/blog-status-unarchived/{title}")
    @PreAuthorize("hasRole('MODERATOR')")
    public ModelAndView changeBlogStatusUnarchived(@PathVariable String title, ModelAndView modelAndView){
        this.blogService.changeBlogStatus(title,"UNARCHIVED");
        modelAndView.setViewName("redirect:/blog");
        return modelAndView;
    }
}
