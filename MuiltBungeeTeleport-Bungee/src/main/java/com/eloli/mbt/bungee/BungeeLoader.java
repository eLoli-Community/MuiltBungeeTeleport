package com.eloli.mbt.bungee;

import com.eloli.mbt.core.MbtCore;
import com.eloli.mbt.core.PlatformAdapter;
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
        core = new MbtCore(this,getDataFolder().toString());
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
    @Override
    public void info(String message) {
        getLogger().info(message);
    }

    @Override
    public void info(String message, Exception exception) {
        getLogger().info(message);
        exception.printStackTrace();
    }

    @Override
    public void warn(String message) {
        getLogger().warning(message);
    }

    @Override
    public void warn(String message, Exception exception) {
        getLogger().warning(message);
        exception.printStackTrace();
    }
}
