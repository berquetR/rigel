package ch.epfl.rigel.astronomy;

import java.util.*;

public final class Types {

    private final Map < String, Integer > unit;




    private Types(double number, Map < String, Integer > units) {

        this.unit = Collections.unmodifiableMap(new TreeMap <>(units));
    }

    private  Types  (double number, Map < String, Integer > units, double d) {
         this (number, units);
    }


    @Override
    public boolean equals (Object that ) {

        Types that1 = (Types) that;

        return this.unit == ((Types) that).unit;
    }

    public static void main(String[] args) {
      Set <Integer> s =new HashSet <>(Arrays.asList(1,2,1));
        System.out.println(s);

    }
}
