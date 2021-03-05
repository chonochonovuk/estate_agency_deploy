package com.ecoverde.estateagency.web;

import com.ecoverde.estateagency.model.binding.PropertyAddBindingModel;
import com.ecoverde.estateagency.model.service.PropertyServiceModel;
import com.ecoverde.estateagency.service.AddressService;
import com.ecoverde.estateagency.service.PropertyService;
import com.ecoverde.estateagency.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/property-add")
public class AgentController {
    private final PropertyService propertyService;
    private final AddressService addressService;
    private final UserService userService;


    public AgentController(PropertyService propertyService, AddressService addressService, UserService userService) {
        this.propertyService = propertyService;
        this.addressService = addressService;
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('AGENT')")
    public String addNewProperty(Model model){
        if (!model.containsAttribute("propertyAddBindingModel")){
            model.addAttribute("propertyAddBindingModel",new PropertyAddBindingModel());
        }
        return "property-new";
    }

    @PostMapping
   @PreAuthorize("hasRole('AGENT')")
    public ModelAndView addNewPropertyConfirm(@Valid @ModelAttribute("propertyAddBindingModel") PropertyAddBindingModel propertyAddBindingModel,
                                              BindingResult bindingResult,
                                              RedirectAttributes redirectAttributes,
                                              ModelAndView modelAndView) throws IOException {
        if (bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("propertyAddBindingModel",propertyAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.propertyAddBindingModel",bindingResult);
            modelAndView.setViewName("redirect:/property-add");
            return modelAndView;
        }

        if (this.propertyService.findByPropertyName(propertyAddBindingModel.getPropertyName()) != null){
            bindingResult.rejectValue("propertyName","error.propertyName","This property name already exist!");
            redirectAttributes.addFlashAttribute("propertyAddBindingModel",propertyAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.propertyAddBindingModel",bindingResult);
            modelAndView.setViewName("redirect:/property-add");
            return modelAndView;
        }

        if (this.userService.findByUsername(propertyAddBindingModel.getUsername()) == null){
            bindingResult.rejectValue("username","error.username","This username not exist!");
            redirectAttributes.addFlashAttribute("propertyAddBindingModel",propertyAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.propertyAddBindingModel",bindingResult);
        }

        if (this.addressService.findByFullAddress(propertyAddBindingModel.getFullAddress()) != null){
            bindingResult.rejectValue("fullAddress","error.fullAddress","This address already exist!");
            redirectAttributes.addFlashAttribute("propertyAddBindingModel",propertyAddBindingModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.propertyAddBindingModel",bindingResult);
        }
        PropertyServiceModel propertyServiceModel = this.propertyService.mapBindingModelToService(propertyAddBindingModel);
        this.propertyService.addProperty(propertyServiceModel);
        modelAndView.setViewName("redirect:/property");
        return modelAndView;
    }
}
