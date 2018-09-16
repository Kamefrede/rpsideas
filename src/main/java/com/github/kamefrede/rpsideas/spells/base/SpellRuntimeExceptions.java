package com.github.kamefrede.rpsideas.spells.base;

public class SpellRuntimeExceptions extends Exception {

    public static final String EVEN_ROOT_NEGATIVE_NUMBER = "rpsideas.spellerror.nthroot";
    public static final String NON_AXIAL_VECTOR = "rpsideas.spellerror.nonaxial";
    public static final String NEGATIVE_LENGTH = "rpsideas.spellerror.length";


    public SpellRuntimeExceptions(String s){
        super(s);
    }
}
