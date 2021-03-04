package org.springframework.samples.petclinic.sfg;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by ronnen on 04-Mar-2021
 */

@SpringJUnitConfig(classes = {BaseConfig.class, YannyConfig.class})
class YannyWordProducerTest {

    @Autowired
    WordProducer wordProducer;

    @Test
    void getWord() {
        String word = wordProducer.getWord();
        assertEquals("Yanny", word);
    }
}