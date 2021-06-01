package com.eloli.mbt.bungee;

import com.eloli.mbt.core.MbtCore;
import com.eloli.sodioncore.file.BaseFileService;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.ProtocolConstants;

public class BungeeCore implements Listener {
    public static MbtCore core;

    public BungeeCore(BungeeLoader loader){
        core = new MbtCore(loader,new BungeeLogger(loader),new BaseFileService(loader.getDataFolder().toString()));
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        core.onQuit(new BungeePlayer(event.getPlayer()));
    }

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        core.onSwitchServer(new BungeePlayer(event.getPlayer()));
    }

    @EventHandler
    public void onPluginMessageEvent(PluginMessageEvent event) {
        if (event.isCancelled()) {
            return;
        }
        if (event.getSender() instanceof ProxiedPlayer) {
            event.setCancelled(core.onClientMessage(
                    new BungeePlayer((ProxiedPlayer) event.getSender()),
                    event.getTag(),
                    event.getData()));
        } else if (event.getSender() instanceof Server
                && event.getReceiver() instanceof ProxiedPlayer) {
            if (event.getTag().equals(
                    ((ProxiedPlayer) event.getReceiver()).getPendingConnection().getVersion() >= ProtocolConstants.MINECRAFT_1_13 ? "minecraft:brand" : "MC|Brand")) {
                ((ProxiedPlayer) event.getReceiver()).sendData(event.getTag(), event.getData());
                event.setCancelled(true);
                return;
            }
            event.setCancelled(core.onServerMessage(
                    new BungeePlayer((ProxiedPlayer) event.getReceiver()),
                    event.getTag(),
                    event.getData()));
        }
    }
}
