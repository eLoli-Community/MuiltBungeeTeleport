package com.eloli.mbt.bungee;

import com.eloli.mbt.core.PlatformAdapter;
import com.eloli.sodioncore.bungee.SodionCoreBootEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public class BungeeLoader extends Plugin implements PlatformAdapter, Listener {
    public static BungeeLoader instance;

    @Override
    public void onEnable() {
        instance = this;
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
        ProxyServer.getInstance().getPluginManager().registerListener(this, new BungeeCore(this));
    }

    @EventHandler
    public void onLoad(SodionCoreBootEvent event){
        //ProxyServer.getInstance().getPluginManager().registerListener(this, new BungeeCore());
    }

    @Override
    public void registerPluginMessageChannel(String channel) {
        getProxy().registerChannel(channel);
    }
}
