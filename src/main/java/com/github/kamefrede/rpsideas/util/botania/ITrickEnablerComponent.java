package com.github.kamefrede.rpsideas.util.botania;

import vazkii.psi.api.cad.ICADComponent;

public interface ITrickEnablerComponent extends ICADComponent {


    enum EnableResult {
        NOT_ENABLED,
        MISSING_REQUIREMENT,
        SUCCESS;

        public static EnableResult fromBoolean(boolean bool) {
            return bool ? SUCCESS : MISSING_REQUIREMENT;
        }
    }
}
