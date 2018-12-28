package com.kamefrede.rpsideas.rules;

/**
 * @author WireSegal
 * Created at 9:07 PM on 12/27/18.
 */
public class TrickRule implements ISpellRule {

    private final String trick;

    public TrickRule(String trick) {
        this.trick = trick;
    }

    @Override
    public boolean isAllowed(String trick) {
        return !this.trick.equals(trick);
    }

    @Override
    public boolean isAllowed(EnumActionType type) {
        return true;
    }
}
