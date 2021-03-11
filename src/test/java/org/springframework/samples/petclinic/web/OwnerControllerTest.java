package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.AfterEach;
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
import static org.mockito.Mockito.reset;
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

    // its mock definition located in the xml file 'mvc-test-config.xml'
    @Autowired
    ClinicService clinicService;

    @InjectMocks
    @Autowired
    OwnerController ownerController;

    MockMvc mockMvc;

    /*
     beforeEach method test, build mockMvc.
     */
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    /*
     the same mock involved in many methods here
     hence, without a 'reset' after every method-test
     an error will arise when running the all-tests-class
     while every test-method works fine.
    */
    @AfterEach
    void tearDown() {
        reset(clinicService);
    }

    /*
     testing the 'get' route "/owners/new".
     using only mockMvc here, no Mocks more than that.
     checking the status isOK(), a new attribute added to model,
     and the return view is as expected.
     */
    @Test
    void initCreationFormTest() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    /*
     testing the 'post' route of "/owners/new".
     checking only the first scenario of the method, entering the 'if' block.
     not including here Owner Object properties populated with values,
     hence, 'BindingResult' will have errors, entering the 'if' block.
     checking status isOK(), and return view is as expected.
    */
    @Test
    void processCreationFormResultHasErrorsTest() throws Exception {
        mockMvc.perform(post("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    /*
     testing the 'post' route of "/owners/new".
     checking here the second scenario of the method, entering the 'else' block.
     Populating the Owner Object properties to hold the '@Valid' restrictions.
     'BindingResult' will have no errors, so the 'else' scenario will occur.
     checking that a 'redirect' is performed to the right route.
     */
    @Test
    void processCreationFormResultNoErrorsTest() throws Exception {
        mockMvc.perform(post("/owners/new")
                    .param("address", "adss")
                    .param("city", "cy")
                    .param("telephone", "000")
                    .param("firstName", "first_name")
                    .param("lastName", "last_name")
                    .param("Id", "1")
                     )
                .andExpect(redirectedUrl("/owners/1"));
    }

    /*
     testing the 'post' route of "/owners/new" with our creation of validation errors.
     checking here a new scenario in which a few owner objects properties missing,
     (removing 'address' and 'city' properties population from the previous test)
     and checking the 'model' object to see if it holds the right errors
     that we created in purpose, related to the validations (@Valid)
     of the Owner object properties.
     Notice the 'model' object (or its mock) is not a parameter to the
     method under test, but its there.
     This is an example of server-side form validation using Spring @Valid
     and the object properties-population, meaning, we are posting to the form,
     we are getting errors, so returning the same url of the form
     (the same view of the form), ideally, the front-end will re-render the form,
     and then posting these errors.
     Example of server-side validation where a request comes to the server,
     and comes back, and then we have the 'error' object to work with in the 'view' layer
     to display the proper error information.
     */
    @Test
    void processCreationFormResultWithOurCreatedErrorsTest() throws Exception {
        mockMvc.perform(post("/owners/new")
                    .param("telephone", "000")
                    .param("firstName", "first_name")
                    .param("lastName", "last_name")
                    .param("Id", "1")
                    )
                .andExpect(model().attributeHasErrors("owner"))
                .andExpect(model().attributeHasFieldErrors("owner","city"))
                .andExpect(model().attributeHasFieldErrors("owner","address"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    /*
         testing the 'get' route of "/owners/find"
         checking the status isOK(), an attribute added to the model,
         and the right view returned.
         */
    @Test
    void initFindFormTest() throws Exception {
        mockMvc.perform(get("/owners/find"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/findOwners"));
    }

    /*
     the following three test methods test 'processFindForm', "/owners" route.
     checking here the first scenario, 'results.isEmpty()' return true
     since there is no definition to the 'clinicService' mock behaviour,
     transferring one parameter to the get() call,
     checking the status isOK(), and the right view returned.
     */
    @Test
    void processFindFormNotFoundTest() throws Exception {
        mockMvc.perform(get("/owners")
                    .param("lastName", "Dont find ME!"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    /*
     testing here the second scenario of 'processFindForm' "/owners" route.
     define a Collection of 'Owners' with one real Owner object within,
     the Collection will be the returned value of 'clinicService' mock
     on the method 'findOwnerByLastName' as we defined that in the 'given' code.
     verifying the mock had one call on this method, and moving to mockMvc,
     checking with mockMvc the redirect route is right, and includes the 'id'
     of the Owner object that we defined. (value 1)
     */
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

    /*
     testing here the third scenario of 'processFindForm' "/owners" route.
     here, a need to have more than one Owner object in the 'results' Collection,
     hence, creating two Owners, and inserting them into the Collection.
     the Collection will be the returned value of 'clinicService' mock
     as defined in the 'given' code on the call to 'findOwnerByLastName'.
     verify the 'clinicService' mock had one call on 'findOwnerByLastName',
     that the status isOK(), an attribute added to the model,
     and the return view is right.
     */
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

    /*
     testing the 'get' route "/owners/{ownerId}/edit".
     define the 'clinicService' mock returned value on 'findOwnerById' call,
     and verify this mock interaction.
     mockMvc 'perform' here on this 'get' route, since we have a @PathVariable
     we pass the value '1' as argument to 'perform', as a @PathVariable value.
     checking the status isOK(), an attribute added,
     and the right view returned.
     */
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

    /*
     testing the first scenario of the 'post' route "/owners/{ownerId}/edit".
     entering the 'if' block since result.hasErrors() is true, meaning,
     the BindingResult object return true since there are no values to
     the Owner object properties, which is under @Valid, and all of its properties
     must include a value. (as they defined)
     mockMvc 'perform' here on this 'post' route. since we have a @PathVariable
     we pass the value '1' as argument to 'perform', as a @PathVariable value.
     checking the status isOK(), and the right view returned.
     */
    @Test
    void processUpdateOwnerFormHasErrorsTest() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    /*
     testing the second scenario of the 'post' route "/owners/{ownerId}/edit".
     mockMvc 'perform' here on this 'post' route. since we have a @PathVariable
     we pass the value '1' as argument to 'perform', as a @PathVariable value.
     mockMvc pass parameters to populate the Owner object properties in the method.
     all needed properties according to their definitions populated with values,
     hence, the BindingResult object return 'false' on 'hasErrors()' call,
     and entering the 'else' block. (the @Valid pass)
     This is one of the reasons of using mockMvc, to see if the Object populated
     properly inside a method, as expected.
     NOTICE the 'param' passed to the 'id' property must defined as 'Id'
     it's not working with 'id'. important!
     passing Owner object properties-values include 'id' equals to 2,
     to see that 'id' value indeed changes inside the method to the @PathVariable
     value that defined to be 1. (in the line 'owner.setId(ownerId)' )
     checking the right redirect view return. (includes the '1' value)
     */
    @Test
    void processUpdateOwnerFormNoErrorsTest() throws Exception {
        mockMvc.perform(post("/owners/{ownerId}/edit", 1)
                    .param("firstName", "name")
                    .param("lastName", "last")
                    .param("address", "addre")
                    .param("city", "city")
                    .param("telephone", "000")
                    .param("Id", "2")
                     )
                .andExpect(redirectedUrl("/owners/1"));
    }

    /*
     testing the route "/owners/{ownerId}". ('showOwner' method)
     define here 'clinicService' (mock) on 'findOwnerById' call to return
     a pre-defined Owner object. verify the mock interactions.
     mockMvc 'perform' here on this route. since we have a @PathVariable
     we pass the value '1' as argument to 'perform', as a @PathVariable value.
     checking the status isOK().
     */
    @Test
    void showOwnerTest() throws Exception {
        Owner owner = new Owner();
        owner.setId(1);

        // given
        given(clinicService.findOwnerById(anyInt())).willReturn(owner);

        // when
        ownerController.showOwner(1);

        // then
        then(clinicService).should(times(1)).findOwnerById(anyInt());

        mockMvc.perform(get("/owners/{ownerId}", 1))
                .andExpect(status().isOk());
    }
}