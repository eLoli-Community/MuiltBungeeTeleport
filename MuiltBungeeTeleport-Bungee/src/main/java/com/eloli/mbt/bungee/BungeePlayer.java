package com.eloli.mbt.bungee;

import com.eloli.mbt.core.AbstractPlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BungeePlayer extends AbstractPlayer {
    private final ProxiedPlayer handle;

    public BungeePlayer(ProxiedPlayer handle) {
        super(handle.getName(), handle.getUniqueId());
        this.handle = handle;
    }

    @Override
    public void sendMessage(String message) {
        handle.sendMessage(message);
    }

    @Override
    public void sendServerData(String channel, byte[] data) {
        handle.getServer().sendData(channel, data);
    }

    @Override
    public void sendClientData(String channel, byte[] data) {
        handle.sendData(channel, data);
    }

    @Override
    public String getCurrent() {
        return handle.getServer().getInfo().getName();
    }

    @Override
    public void teleport(String destination) {
        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(destination);
        if (serverInfo == null) {
            handle.sendMessage("No such server");
        } else {
            handle.connect(serverInfo);
        }
    }

    @Override
    public boolean canMbt() {
        return handle.hasPermission("mbt.tp");
    }

    @Override
    public boolean isOnline() {
        return handle.isConnected();
    }
}
