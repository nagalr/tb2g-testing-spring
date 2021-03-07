package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

    @Mock
    ClinicService clinicService;

    @Mock
    Map<String, Object> model;

    @InjectMocks
    VetController controller;

    List<Vet> vetsList = new ArrayList<>();

    // declare the MockMvc
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        vetsList.add(new Vet());

        given(clinicService.findVets()).willReturn(vetsList);

        // build the mock using the static method, based on 'controller'
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    /*
     Testing the MockMvc here.
     perform a 'get' against the given url "/vets.html"
     - verify that the return status is 'OK'
     - verify that the model added the attribute 'vets'
     - verify that the controller returned the expected view: "vets/vetList".
     This test uses SpringMVC framework with Mock Objects to exercise the dispatcher servlet,
     find the url, uses SpringMVC model, takes the response back from the controller
     and return it to our test, where we can make the assertions to check status (OK),
     that the needed model attribute indeed added, and the return view name is as expected.
     */
    @Test
    void testControllerShowList() throws Exception {
        mockMvc.perform(get("/vets.html"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("vets"))
                .andExpect(view().name("vets/vetList"));
    }

    @Test
    void showVetList() {
        //when
        String view = controller.showVetList(model);

        //then
        then(clinicService).should().findVets();
        then(model).should().put(anyString(), any());
        assertThat(view).isEqualToIgnoringCase("vets/VetList");
    }

    @Test
    void showResourcesVetList() {
        //when
        Vets vets = controller.showResourcesVetList();

        //then
        then(clinicService).should().findVets();
        assertThat(vets.getVetList()).hasSize(1);
    }
}