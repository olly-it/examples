package it.olly.lookup;

import org.junit.jupiter.api.Test;

import it.olly.lookup.MyServiceFactory;
import it.olly.lookup.MyServiceInterface;

public class LookupTester {

    @Test
    public void testLookup() {
        MyServiceInterface service = MyServiceFactory.getService("one");
        System.out.println("> " + service.compute("ciao"));
    }
}
