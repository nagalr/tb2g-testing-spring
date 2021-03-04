package org.springframework.samples.petclinic.sfg;

import org.springframework.stereotype.Component;

/**
 * Created by ronnen on 04-Mar-2021
 */

@Component
public class YannyWordProducer implements WordProducer {
    @Override
    public String getWord() {
        return "Yanny";
    }
}
