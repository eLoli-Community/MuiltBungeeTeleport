package com.eloli.mbt.core.config;

public abstract class Migrater {
    public abstract void migrate(Configure from, Configure to);
}
