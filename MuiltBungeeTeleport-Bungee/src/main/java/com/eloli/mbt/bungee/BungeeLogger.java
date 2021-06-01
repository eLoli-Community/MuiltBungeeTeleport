package com.eloli.mbt.bungee;

import com.eloli.sodioncore.logger.AbstractLogger;

public class BungeeLogger implements AbstractLogger {
    public BungeeLoader loader;

    public BungeeLogger(BungeeLoader loader){
        this.loader=loader;
    }
    @Override
    public void info(String info) {
        loader.getLogger().info(info);
    }

    @Override
    public void info(String info, Exception exception) {
        loader.getLogger().info(info);
        exception.printStackTrace();
    }

    @Override
    public void warn(String info) {
        loader.getLogger().warning(info);
    }

    @Override
    public void warn(String info, Exception exception) {
        loader.getLogger().warning(info);
        exception.printStackTrace();
    }
}