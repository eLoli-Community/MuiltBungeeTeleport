package com.eloli.mbt.bungee;

import com.eloli.mbt.core.MbtCore;
import com.eloli.mbt.core.PlatformAdapter;
import com.eloli.sodioncore.file.BaseFileService;
import com.eloli.sodioncore.logger.AbstractLogger;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.ProtocolConstants;

public class BungeeLoader extends Plugin implements PlatformAdapter, Listener {
    public static BungeeLoader instance;
    public static MbtCore core;
    @Override
    public void onEnable() {
        instance = this;
        core = new MbtCore(this, new AbstractLogger() {
            @Override
            public void info(String info) {
                getLogger().info(info);
            }

            @Override
            public void info(String info, Exception exception) {
                getLogger().info(info);
                exception.printStackTrace();
            }

            @Override
            public void warn(String info) {
                getLogger().warning(info);
            }

            @Override
            public void warn(String info, Exception exception) {
                getLogger().warning(info);
                exception.printStackTrace();
            }
        },new BaseFileService(getDataFolder().toString()));
        ProxyServer.getInstance().getPluginManager().registerListener(this,this);
    }

    public void onQuit(PlayerDisconnectEvent event){
        core.onQuit(new BungeePlayer(event.getPlayer()));
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event){
        core.onSwitchServer(new BungeePlayer(event.getPlayer()));
    }

    @Override
    public void registerPluginMessageChannel(String channel) {
        getProxy().registerChannel(channel);
    }
    @EventHandler
    public void onPluginMessageEvent(PluginMessageEvent event) {
        if(event.isCancelled()){
            return;
        }
        if (event.getSender() instanceof ProxiedPlayer) {
            event.setCancelled(core.onClientMessage(
                    new BungeePlayer((ProxiedPlayer)event.getSender()),
                            event.getTag(),
                            event.getData()));
        } else if (event.getSender() instanceof Server
                && event.getReceiver() instanceof ProxiedPlayer) {
            if(event.getTag().equals(
                    ((ProxiedPlayer) event.getReceiver()).getPendingConnection().getVersion() >= ProtocolConstants.MINECRAFT_1_13 ? "minecraft:brand" : "MC|Brand" )){
                ((ProxiedPlayer) event.getReceiver()).sendData(event.getTag(), event.getData());
                event.setCancelled(true);
                return;
            }
            event.setCancelled(core.onServerMessage(
                    new BungeePlayer((ProxiedPlayer)event.getReceiver()),
                    event.getTag(),
                    event.getData()));
        }
    }
}
