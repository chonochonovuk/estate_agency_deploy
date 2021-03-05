package com.ecoverde.estateagency.web;

import com.ecoverde.estateagency.model.binding.AjaxResponseBody;
import com.ecoverde.estateagency.model.binding.PropertySearchModel;
import com.ecoverde.estateagency.model.view.PropertyViewModel;
import com.ecoverde.estateagency.service.AddressService;
import com.ecoverde.estateagency.service.PropertyService;
import com.ecoverde.estateagency.service.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@CrossOrigin(origins = "/property")
@RequestMapping("/search")
public class SearchController {
    private final PropertyService propertyService;
    private final AddressService addressService;
    private final TownService townService;
    private final ModelMapper modelMapper;

    @Autowired
    public SearchController(PropertyService propertyService, AddressService addressService, TownService townService, ModelMapper modelMapper) {
        this.propertyService = propertyService;
        this.addressService = addressService;
        this.townService = townService;
        this.modelMapper = modelMapper;
    }

   @PostMapping
    public ResponseEntity<AjaxResponseBody> getSearchResultWithAjax(@RequestBody PropertySearchModel propertySearchModel){
        AjaxResponseBody result = new AjaxResponseBody();
        Set<PropertyViewModel> findProperty = this.propertyService.findAllProperties(propertySearchModel);

       if (findProperty.isEmpty()){
          result.setMessage("No matching results!!!");
       }else {
           result.setMessage("Success");
           result.setResult(findProperty);
       }
        return ResponseEntity.ok(result);
   }


}
