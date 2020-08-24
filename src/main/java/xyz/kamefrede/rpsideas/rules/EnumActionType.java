package xyz.kamefrede.rpsideas.rules;

import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;

/**
 * @author WireSegal
 * Created at 9:04 PM on 12/27/18.
 */
public enum EnumActionType {
    EDIT_BLOCK,
    SPAWN_ENTITY,
    HARM_ENTITY,
    APPLY_POTION;

    private static final Map<String, EnumActionType> actions = Maps.newHashMap();

    static {
        for (EnumActionType type : values())
            actions.put(type.getName(), type);
    }

    @Nullable
    public static EnumActionType byName(String name) {
        return actions.get(name);
    }

    public String getName() {
        return name().toLowerCase(Locale.ROOT);
    }
}
