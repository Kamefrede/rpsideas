package com.github.kamefrede.rpsideas.spells.base;

public class SpellRuntimeExceptions extends Exception {

    public static final String EVEN_ROOT_NEGATIVE_NUMBER = "rpsideas.spellerror.nthroot";
    public static final String NON_AXIAL_VECTOR = "rpsideas.spellerror.nonaxial";
    public static final String NEGATIVE_LENGTH = "rpsideas.spellerror.length";
    public static final String NULL_NUMBER = "rpsideas.spellerror.movement";
    public static final String OUT_OF_BOUNDS = "rpsideas.spellerror.outofbounds";
    public static final String NULL_LIST = "rpsideas.spellerror.nulllist";
    public static final String ENTITY_NOT_LIVING = "rpsideas.spellerror.notliving";
    public static final String NO_GROUND = "rpsideas.spellerror.noground";
    public static final String VOLUME = "rpsideas.spellerror.volume";
    public static final String INSTRUMENTS = "rpsideas.spellerror.instruments";
    public static final String PITCH = "rpsideas.spellerror.pitch";
    public static final String ARMOR = "rpsideas.spellerror.armor";

    public SpellRuntimeExceptions(String s){
        super(s);
    }
}
