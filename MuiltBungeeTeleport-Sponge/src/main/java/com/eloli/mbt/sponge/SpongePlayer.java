package com.eloli.mbt.sponge;

import com.eloli.mbt.core.AbstractPlayer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class SpongePlayer extends AbstractPlayer {

    public Player handle;

    public SpongePlayer(Player handle) {
        super(handle.getName(), handle.getUniqueId());
        this.handle = handle;
    }

    @Override
    public void sendMessage(String message) {
        handle.sendMessage(Text.of(message));
    }

    @Override
    public void sendServerData(String channel, byte[] data) {
        //
    }

    @Override
    public void sendClientData(String channel, byte[] data) {
        Sponge.getChannelRegistrar()
                .getOrCreateRaw(this, channel)
                .sendTo(handle, channelBuf -> channelBuf.writeBytes(data));
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
        return handle.hasPermission("mbt.tp");
    }

    @Override
    public boolean isOnline() {
        return handle.isOnline();
    }
}
