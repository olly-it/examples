package it.olly.lookup.impl;

import it.olly.lookup.MyServiceInterface;

public class MyServiceTwo implements MyServiceInterface {

    @Override
    public String compute(String input) {
        return "222 [" + input + "] 222";
    }

    @Override
    public String type() {
        return "two";
    }

}
