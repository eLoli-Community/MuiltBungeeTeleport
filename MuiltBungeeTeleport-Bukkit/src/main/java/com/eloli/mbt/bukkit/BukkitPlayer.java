package com.eloli.mbt.bukkit;

import com.eloli.mbt.core.AbstractPlayer;
import org.bukkit.entity.Player;

public class BukkitPlayer extends AbstractPlayer {
    private final Player handle;
    public BukkitPlayer(Player handle){
        super(handle.getName(), handle.getUniqueId());
        this.handle=handle;
    }
    @Override
    public void sendMessage(String message) {
        handle.sendMessage(message);
    }

    @Override
    public void sendServerData(String channel, byte[] data) {
        //
    }

    @Override
    public void sendClientData(String channel, byte[] data) {
        handle.sendPluginMessage(BukkitLoader.instance, channel, data);
    }

    @Override
    public String getCurrent() {
        return null;
    }

    @Override
    public void teleport(String destination) {
        //
    }

    @Override
    public boolean canMbt() {
        if(handle.isOp()){
            return true;
        }
        return handle.hasPermission("mbt.tp");
    }

    @Override
    public boolean isOnline() {
        return handle.isOnline();
    }
}
