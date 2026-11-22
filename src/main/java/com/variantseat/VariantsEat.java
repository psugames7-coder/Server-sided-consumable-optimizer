package com.variantseat;

import net.fabricmc.api.ModInitializer;

public final class VariantsEat implements ModInitializer {

    public static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger("Server Sided Eating Optimizer");

    @Override
    public void onInitialize() {
        LOGGER.info("Server Sided Eating Optimizer loaded");
    }
}
