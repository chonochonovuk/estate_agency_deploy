package com.ecoverde.estateagency.web;


import com.ecoverde.estateagency.service.PropertyTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class PropertyController {


    private final PropertyTypeService propertyTypeService;

    @Autowired
    public PropertyController(PropertyTypeService propertyTypeService) {
        this.propertyTypeService = propertyTypeService;
    }

    @GetMapping("/property")
    public String properties(Model model){

        model.addAttribute("allPropertyTypes",this.propertyTypeService.findAllTypes());
        return "properties";
    }

    @GetMapping("/single")
    public String propertySingle(Model model){

        return "properties-single";
    }
}
