package com.github.kamefrede.rpsideas.util.botania;

public enum EnumManaTier {
    BASE,
    ALFHEIM;

    public static boolean allowed(EnumManaTier cadTier, EnumManaTier pieceTier) {
        return cadTier.ordinal() >= pieceTier.ordinal();
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }

}
