/*
 * Copyright (c) 2025 DenaryDev
 *
 * Use of this source code is governed by an MIT-style
 * license that can be found in the LICENSE file or at
 * https://opensource.org/licenses/MIT.
 */
package me.denarydev.chromium;

import me.denarydev.chromium.config.ChromiumConfig;
import me.denarydev.chromium.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ChromiumMod implements ModInitializer {
    public static final String MOD_ID = "chromium";
    public static final Logger LOGGER = LoggerFactory.getLogger("Chromium");

    private static final ConfigManager CONFIG_MANAGER = new ConfigManager();

    @Override
    public void onInitialize() {
        CONFIG_MANAGER.readConfig(false);
    }

    public static ConfigManager configManager() {
        return CONFIG_MANAGER;
    }

    public static ChromiumConfig config() {
        return CONFIG_MANAGER.getConfig();
    }
}
