package com.ecoverde.estateagency.web;

import com.ecoverde.estateagency.service.PropertyService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PropertyDetailsController {
    private final PropertyService propertyService;
    private final ModelMapper modelMapper;

    @Autowired
    public PropertyDetailsController(PropertyService propertyService, ModelMapper modelMapper) {
        this.propertyService = propertyService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/property-details/{propertyName}")
    public String propertyDetails(@PathVariable String propertyName, Model model){
        model.addAttribute("propertyDetails",this.propertyService.findByPropertyName(propertyName));
        return "properties-single";
    }
}
