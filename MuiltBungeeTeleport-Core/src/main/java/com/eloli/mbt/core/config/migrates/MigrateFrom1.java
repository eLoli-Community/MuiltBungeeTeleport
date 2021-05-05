package com.eloli.mbt.core.config.migrates;

import com.eloli.mbt.core.config.Configure;
import com.eloli.mbt.core.config.MainConfiguration;
import com.eloli.mbt.core.config.Migrater;

public class MigrateFrom1 extends Migrater {
    @Override
    public void migrate(Configure pFrom, Configure pTo) {
        MainConfiguration1 form = (MainConfiguration1) pFrom;
        MainConfiguration to = (MainConfiguration) pTo;
    }
}
