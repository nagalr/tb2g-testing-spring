package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

    BindingResult bindingResult = mock(BindingResult.class);

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
    void testFindByNameNotFound() throws Exception {
        mockMvc.perform(get("/owners")
                    .param("lastName", "Dont find ME!"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void initCreationFormTest() throws Exception {
        mockMvc.perform(get("/owners/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("owner"))
            .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void processCreationFormResultEmptyTest() throws Exception {
        // given
        given(bindingResult.hasErrors()).willReturn(true);

        // when
        ownerController.processCreationForm(new Owner(), bindingResult);

        // then
        then(bindingResult).should(times(1)).hasErrors();

        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void processCreationFormResultNotEmptyTest() throws Exception {
        Owner owner = new Owner();
        owner.setAddress("");
        owner.setCity("");
        owner.setTelephone("");
        owner.setFirstName("");
        owner.setLastName("");
        owner.setId(1);

        // given
        given(bindingResult.hasErrors()).willReturn(false);

        // when
        ownerController.processCreationForm(owner, bindingResult);

        //then
        mockMvc.perform(get("/owners/new")
                    .param("id", "1"))
                .andExpect(redirectedUrl("/owners/1"));
    }

    //    @Test
//    void processCreationFormResultOneItemTest() throws Exception {
//        Collection<Owner> results = new HashSet<>();
//        Owner owner = new Owner();
//        owner.setAddress("");
//        owner.setCity("");
//        owner.setTelephone("");
//        owner.setFirstName("");
//        owner.setLastName("");
//        owner.setId(1);
//        results.add(owner);
//
//        // given
//        given(bindingResult.hasErrors()).willReturn(false);
//        given(clinicService.findOwnerByLastName(anyString())).willReturn(results);
//
//        // when
//        ownerController.processFindForm(owner, bindingResult, new HashMap<>());
//
//        // then
//        then(bindingResult).should(times(1)).hasErrors();
//        then(bindingResult).shouldHaveNoMoreInteractions();
//
//        mockMvc.perform(get("/owners"))
//                .andExpect(redirectedUrl("/owners/1"));
//    }
}