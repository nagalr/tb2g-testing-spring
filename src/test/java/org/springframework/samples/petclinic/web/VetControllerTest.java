package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by ronnen on 03-Mar-2021
 */

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

    @Mock
    private ClinicService clinicService;

    @InjectMocks
    private VetController vetController;

    @Test
    void showVetList() {

        // given
        Collection<Vet> vets = new ArrayList<>();
        Map<String, Object> objectMap = new HashMap<>();
        given(clinicService.findVets()).willReturn(vets);

        // when
        Collection<Vet> vetresult = clinicService.findVets();
        String result = vetController.showVetList(objectMap);

        // then
        assertThat(vetresult).isNotNull();
        then(clinicService).should(times(2)).findVets();
        assertEquals("vets/vetList", result);
    }

    // this is just an example
    @Test
    void showResourcesVetList() {
        // given
        Collection<Vet> vetCollection = new HashSet<>();
        given(clinicService.findVets()).willReturn(vetCollection);

        // when
        Vets result = vetController.showResourcesVetList();

        // then
        assertThat(result).isNotNull();
        verify(clinicService, times(1)).findVets();
    }
}

