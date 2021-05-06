package com.olly.lookup;

import org.junit.jupiter.api.Test;

public class LookupTester {

    @Test
    public void testLookup() {
        MyServiceInterface service = MyServiceFactory.getService("one");
        System.out.println("> " + service.compute("ciao"));
    }
}
