package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    Model model;

    @Mock
    BindingResult bindingResult;

    // its mock definition located in the xml file
    @Autowired
    ClinicService clinicService;

    @InjectMocks
    @Autowired
    OwnerController ownerController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @Test
    void initCreationFormTest() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    /*
     not including here Owner Object properties populated with values,
     hence, 'BindingResult' will have errors, entering the 'if' block.
    */
    @Test
    void processCreationFormResultHasErrorsTest() throws Exception {
        mockMvc.perform(post("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    /*
     Populating the Owner Object properties to hold the '@Valid' restrictions.
     'BindingResult' will have no errors, so the 'else' scenario will occur.
     */
    @Test
    void processCreationFormResultNoErrorsTest() throws Exception {
        mockMvc.perform(post("/owners/new")
                    .param("address", "adss")
                    .param("city", "cy")
                    .param("telephone", "000")
                    .param("firstName", "first_name")
                    .param("lastName", "last_name")
                    .param("Id","1")
                        )
                .andExpect(redirectedUrl("/owners/1"));
    }

    @Test
    void initFindFormTest() throws Exception {
        mockMvc.perform(get("/owners/find"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void processFindFormNotFoundTest() throws Exception {
        mockMvc.perform(get("/owners")
                    .param("lastName", "Dont find ME!"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void processFindFormFoundOneTest() throws Exception {
        Collection<Owner> owners = new HashSet<>();
        Owner owner = new Owner();
        owner.setId(1);

        owners.add(owner);

        // given
        given(clinicService.findOwnerByLastName(anyString())).willReturn(owners);

        // when
        ownerController.processFindForm(new Owner(), bindingResult, new HashMap<>());

        // then
        then(clinicService).should(times(1)).findOwnerByLastName(anyString());

        mockMvc.perform(get("/owners"))
                .andExpect(redirectedUrl("/owners/1"));
    }

    @Test
    void processFindFormFoundMultipleTest() throws Exception {
        Collection<Owner> owners = new HashSet<>();
        Owner owner = new Owner();
        Owner owner2 = new Owner();

        owners.add(owner);
        owners.add(owner2);

        // given
        given(clinicService.findOwnerByLastName(anyString())).willReturn(owners);

        // when
        ownerController.processFindForm(new Owner(), bindingResult, new HashMap<>());

        // then
        then(clinicService).should(times(1)).findOwnerByLastName(anyString());

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("selections"))
                .andExpect(view().name("owners/ownersList"));
    }

    @Test
    void initUpdateOwnerFormTest() throws Exception {
        // given
        given(clinicService.findOwnerById(anyInt())).willReturn(new Owner());

        // when
        ownerController.initUpdateOwnerForm(1, model);

        // then
        then(clinicService).should(times(1)).findOwnerById(anyInt());

        mockMvc.perform(get("/owners/{ownerId}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void processUpdateOwnerFormHasErrorsTest() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }
}