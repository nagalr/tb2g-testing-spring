package org.springframework.samples.petclinic.sfg;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by ronnen on 04-Mar-2021
 */


public class HearingInterpreterYannyTest {

    HearingInterpreter hearingInterpreter;

    @Before
    public void setUp() throws Exception {
        hearingInterpreter = new HearingInterpreter(new YannyWordProducer());
    }

    @Test
    public void whatIheard() {
        String result = hearingInterpreter.whatIheard();
        assertEquals("Yanny", result);
    }
}
