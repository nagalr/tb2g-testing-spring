package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ClinicService;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.BDDMockito.given;

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
        Vet vet1 = new Vet();
        Vet vet2 = new Vet();

        Collection<Vet> vets = new ArrayList<>();

        vets.add(vet1);
        vets.add(vet2);

        // given
        given(clinicService.findVets()).willReturn(vets);


        // when

        // then
    }

    // this is just an example
    @Test
    void showResourcesVetList() {

    }
}

