package it.olly.lookup.impl;

import it.olly.lookup.MyServiceInterface;

public class MyServiceOne implements MyServiceInterface {

    @Override
    public String compute(String input) {
        return "111 [" + input + "] 111";
    }

    @Override
    public String type() {
        return "one";
    }

}
