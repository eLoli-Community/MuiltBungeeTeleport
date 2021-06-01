package com.eloli.mbt.core.channel.clientPacket;

import com.eloli.sodioncore.channel.ClientPacket;
import com.eloli.sodioncore.channel.util.FieldWrapper;
import com.eloli.sodioncore.channel.util.Priority;

import java.util.List;

public class DeeperTeleportPacket extends ClientPacket {
    public static List<FieldWrapper> fieldWrapperList = resolveFieldWrapperList(DeeperTeleportPacket.class);

    @Priority(0)
    public String destination;

    @Priority(1)
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
