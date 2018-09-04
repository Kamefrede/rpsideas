package com.github.kamefrede.rpsideas.spells.base;

public class SpellRuntimeExceptions extends Exception {

    public static final String EVEN_ROOT_NEGATIVE_NUMBER = "rpsideas.spellerror.nthroot";


    public SpellRuntimeExceptions(String s){
        super(s);
    }
}
