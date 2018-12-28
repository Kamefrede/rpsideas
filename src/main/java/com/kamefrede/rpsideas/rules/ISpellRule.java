package com.kamefrede.rpsideas.rules;

/**
 * @author WireSegal
 * Created at 10:06 AM on 12/27/18.
 */
public interface ISpellRule {
    boolean isAllowed(String trick);

    boolean isAllowed(EnumActionType type);
}
