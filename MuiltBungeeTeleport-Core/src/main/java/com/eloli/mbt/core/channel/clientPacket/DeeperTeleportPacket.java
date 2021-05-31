package com.eloli.mbt.core.channel.clientPacket;

import com.eloli.mbt.core.channel.serverPacket.ShakeTokenPacket;
import com.eloli.sodioncore.channel.ClientPacket;
import com.eloli.sodioncore.channel.util.FieldWrapper;

import java.util.List;

public class DeeperTeleportPacket extends ClientPacket {
    public static List<FieldWrapper> fieldWrapperList = resolveFieldWrapperList(ShakeTokenPacket.class);

    public String destination;
    public String token;

    public DeeperTeleportPacket() {
    }

    public DeeperTeleportPacket(String destination, String token) {
        this.destination = destination;
        this.token = token;
    }

    @Override
    public List<FieldWrapper> getFieldWrapperList() {
        return fieldWrapperList;
    }
}
