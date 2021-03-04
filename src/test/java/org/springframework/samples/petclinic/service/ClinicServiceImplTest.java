package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

/**
 * Created by ronnen on 04-Mar-2021
 */

@ExtendWith(MockitoExtension.class)
class ClinicServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private ClinicServiceImpl clinicService;

    @Test
    void findPetTypes() {
        // given
        List<PetType> petTypeList = new ArrayList<>();
        given(petRepository.findPetTypes()).willReturn(petTypeList);

        // when
        Collection<PetType> resultList = clinicService.findPetTypes();

        // then
        then(petRepository).should(times(1)).findPetTypes();
        assertThat(resultList).isNotNull();
    }
}