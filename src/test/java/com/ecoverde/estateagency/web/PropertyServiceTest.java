package com.ecoverde.estateagency.web;

import com.ecoverde.estateagency.model.binding.PropertyAddBindingModel;
import com.ecoverde.estateagency.model.binding.PropertySearchModel;
import com.ecoverde.estateagency.model.entity.Image;
import com.ecoverde.estateagency.model.service.*;
import com.ecoverde.estateagency.model.view.PropertyViewModel;
import com.ecoverde.estateagency.repositories.PropertyRepository;
import com.ecoverde.estateagency.service.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PropertyServiceTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private PropertyService propertyService;
    @Autowired
    private PropertyTypeService propertyTypeService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private TownService townService;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @Before
    public void dataInit() {
        this.roleService.addRole("ROLE_USER");
        UserServiceModel user = new UserServiceModel();
        user.setUsername("pesho");
        user.setPassword("pesho2");
        user.setFirstName("Pesho");
        user.setLastName("Radinov");
        user.setPhoneNumber("0884885077");
        this.userService.register(user);

        TownServiceModel t2 = new TownServiceModel();
        t2.setName("Burgas");
        this.townService.addTown(t2);
        AddressServiceModel addr2 = new AddressServiceModel();
        addr2.setArea("Sveti Vlas");
        addr2.setFullAddress("13 Pirin street");
        this.addressService.addAddress(addr2);
        PropertyTypeServiceModel pt2 = new PropertyTypeServiceModel();
        pt2.setTypeName("House");
        this.propertyTypeService.addPropertyType(pt2);
        PropertyServiceModel prop2 = new PropertyServiceModel();
        prop2.setPropertyName("Sveti Vlas Pirin");
        prop2.setPropertyTypeServiceModel(pt2);
        prop2.setTownServiceModel(t2);
        prop2.setAddressServiceModel(addr2);
        prop2.setDescription("This is a beautiful and spacious three bedrooms house with spectacular " +
                "sea views across the bay to the ancient town of Nessebar." +
                " It is situated in the beautiful and salubrious 'Garden of Eden'," +
                " in the upmarket town of St Vlas.");
        prop2.setPrice(new BigDecimal(125000));
        prop2.setBathrooms(2);
        prop2.setRooms(5);
        prop2.setDate(LocalDate.parse("2020-07-17", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        prop2.setYear(2006);
        prop2.setSize(242);
        Image img3 = new Image();
        img3.setUrl("/images/burgas/stVlas/house1.jpg");
        prop2.setPhotos(img3);
        prop2.setOwner(this.userService.findByUsername("pesho"));
        this.propertyService.addProperty(prop2);

    }

    @Test
    public void testFindByPropertyName(){
        PropertyServiceModel propertyServiceModel = this.propertyService.findByPropertyName("Sveti Vlas Pirin");
        PropertyServiceModel propertyServiceModel1 = this.propertyService.findByPropertyName("Sofia Center");
        Assert.assertNotNull(propertyServiceModel);
        Assert.assertNull(propertyServiceModel1);
    }

    @Test
    public void testPropertyAddress(){
        PropertyServiceModel propertyServiceModel = this.propertyService.findByPropertyName("Sveti Vlas Pirin");
        Assert.assertNotNull(propertyServiceModel);
        Assert.assertEquals("13 Pirin street",propertyServiceModel.getAddressServiceModel().getFullAddress());
    }

    @Test
    public void testPropertyTown(){
        PropertyServiceModel propertyServiceModel = this.propertyService.findByPropertyName("Sveti Vlas Pirin");
        Assert.assertNotNull(propertyServiceModel);
        Assert.assertEquals("Burgas",propertyServiceModel.getTownServiceModel().getName());
    }

    @Test
    public void testPropertyOwner(){
        PropertyServiceModel propertyServiceModel = this.propertyService.findByPropertyName("Sveti Vlas Pirin");
        Assert.assertNotNull(propertyServiceModel);
        Assert.assertEquals("pesho",propertyServiceModel.getOwner().getUsername());
    }

    @Test
    public void addProperty() throws IOException {
        PropertyAddBindingModel pbm = new PropertyAddBindingModel();
        pbm.setTown("Varna");
        pbm.setArea("Center");
        pbm.setFullAddress("97 Knyaginya Maria Luiza boulevard, third floor, apt. number 12");
        pbm.setPropertyType("Apartment");
        pbm.setPropertyName("Varna center");
        pbm.setDescription("Apartment in the heart of Sofia with two bedrooms and a balcony!");
        pbm.setPrice(new BigDecimal(65000));
        pbm.setBathrooms(1);
        pbm.setRooms(2);
        pbm.setDate(LocalDate.parse("2020-06-22", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        pbm.setYear(2002);
        pbm.setSize(116);
        pbm.setUsername("pesho");
        PropertyServiceModel psm = this.propertyService.mapBindingModelToService(pbm);

        this.propertyService.addProperty(psm);

        Assert.assertNotNull(this.propertyService.findByPropertyName("Varna center"));
    }


    @Test
    public void testPropertySearchEngine(){
        PropertySearchModel psm = new PropertySearchModel();
        psm.setKeyword("apt");
        psm.setLocation("Burgas");
        psm.setPrice(new BigDecimal(150000));
        psm.setPropertyType("house");

        Set<PropertyViewModel> propertyViewModels = this.propertyService.findAllProperties(psm);
        PropertyViewModel propertyViewModel = this.modelMapper.map(this.propertyService.findByPropertyName("Sveti Vlas Pirin"),PropertyViewModel.class);
        for (PropertyViewModel p: propertyViewModels) {

          Assert.assertEquals(p.getPropertyName(),propertyViewModel.getPropertyName());

        }
    }

    @Test
    public void testPropertySearchByKeyword() {

        Set<PropertyViewModel> propertyViewModels = this.propertyService.findByKeyword("apt");
        PropertyViewModel propertyViewModel = this.modelMapper.map(this.propertyService.findByPropertyName("Sveti Vlas Pirin"), PropertyViewModel.class);
        for (PropertyViewModel p : propertyViewModels) {

            Assert.assertEquals(p.getPropertyName(), propertyViewModel.getPropertyName());

        }
    }

    @Test
    public void testPropertySearchByType() {

        Set<PropertyViewModel> propertyViewModels = this.propertyService.findAllByPropertyType("apartment");
        PropertyViewModel propertyViewModel = this.modelMapper.map(this.propertyService.findByPropertyName("Sveti Vlas Pirin"), PropertyViewModel.class);
        for (PropertyViewModel p : propertyViewModels) {

            Assert.assertEquals(p.getPropertyName(), propertyViewModel.getPropertyName());

        }
    }

    @Test
    public void testPropertySearchByPrice() {

        Set<PropertyViewModel> propertyViewModels = this.propertyService.findAllByPrice(new BigDecimal(130000));
        PropertyViewModel propertyViewModel = this.modelMapper.map(this.propertyService.findByPropertyName("Sveti Vlas Pirin"), PropertyViewModel.class);
        for (PropertyViewModel p : propertyViewModels) {

            Assert.assertEquals(p.getPropertyName(), propertyViewModel.getPropertyName());

        }
    }
}
