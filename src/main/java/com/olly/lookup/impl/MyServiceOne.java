package com.olly.lookup.impl;

import com.olly.lookup.MyServiceInterface;

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
