package com.eloli.mbt.core;

import java.util.UUID;

public abstract class AbstractPlayer {
    protected final String name;
    protected final UUID uuid;

    public AbstractPlayer(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }
    public String getName() {
        return name;
    }

    public UUID getUniqueId() {
        return uuid;
    }

    public abstract void sendMessage(String message);

    // as Bungee
    public abstract void sendServerData(String channel, byte[] data);

    // as Bukkit
    public abstract void sendClientData(String channel, byte[] data);

    public abstract String getCurrent();

    public abstract void teleport(String destination);

    public abstract boolean canMbt();

    public abstract boolean isOnline();
}
