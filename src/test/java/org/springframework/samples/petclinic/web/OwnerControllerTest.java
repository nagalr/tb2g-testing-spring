package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// This test-class based on xml configurations.
// the 'clinicService' Mock definition in 'mvc-test-config.xml'
@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    @Mock
    ClinicService clinicService;

    @Mock
    BindingResult bindingResult;

    @InjectMocks
    OwnerController ownerController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @Test
    void testFindByNameNotFound() throws Exception {
        mockMvc.perform(get("/owners")
                .param("lastName", "James"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void testFindByNameFound() throws Exception {

        Collection<Owner> owners = new HashSet<>();

        Owner owner = new Owner();
        owner.setAddress("");
        owner.setCity("");
        owner.setTelephone("");
        owner.setFirstName("");
        owner.setLastName("");
        owner.setId(1);

        owners.add(owner);

        // given
        given(clinicService.findOwnerByLastName(anyString())).willReturn(owners);

        // when
        ownerController.processFindForm(owner, bindingResult, new HashMap<>());

        //then
        then(clinicService).should(times(1)).findOwnerByLastName(anyString());
        then(clinicService).shouldHaveNoMoreInteractions();

        mockMvc.perform(get("/owners"))
                .andExpect(redirectedUrl("/owners/1"));
    }

    @Test
    void testFindByNameFoundTwo() throws Exception {

        Collection<Owner> owners = new HashSet<>();

        Owner owner = new Owner();
        owner.setAddress("");
        owner.setCity("");
        owner.setTelephone("");
        owner.setFirstName("");
        owner.setLastName("");
        owner.setId(1);

        Owner owner2 = new Owner();
        owner2.setAddress("");
        owner2.setCity("");
        owner2.setTelephone("");
        owner2.setFirstName("");
        owner2.setLastName("");
        owner2.setId(2);

        owners.add(owner);
        owners.add(owner2);

        // given
        given(clinicService.findOwnerByLastName(anyString())).willReturn(owners);

        // when
        ownerController.processFindForm(owner, bindingResult, new HashMap<>());

        //then
        then(clinicService).should(times(1)).findOwnerByLastName(anyString());
        then(clinicService).shouldHaveNoMoreInteractions();

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"));
    }

    @Test
    void initCreationFormTest() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }
}