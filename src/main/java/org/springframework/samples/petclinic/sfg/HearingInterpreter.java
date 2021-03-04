package org.springframework.samples.petclinic.sfg;

import org.springframework.stereotype.Service;

/**
 * Created by ronnen on 04-Mar-2021
 */

@Service
public class HearingInterpreter {

    private final WordProducer wordProducer;

    public HearingInterpreter(WordProducer wordProducer) {
        this.wordProducer = wordProducer;
    }

    public String whatIheard(){
        String word = wordProducer.getWord();

        System.out.println(word);

        return word;
    }
}
