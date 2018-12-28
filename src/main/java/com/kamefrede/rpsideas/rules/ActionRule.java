package com.kamefrede.rpsideas.rules;

/**
 * @author WireSegal
 * Created at 9:07 PM on 12/27/18.
 */
public class ActionRule implements ISpellRule {

    private final EnumActionType type;

    public ActionRule(EnumActionType type) {
        this.type = type;
    }

    @Override
    public boolean isAllowed(String trick) {
        return true;
    }

    @Override
    public boolean isAllowed(EnumActionType type) {
        return this.type != type;
    }
}
