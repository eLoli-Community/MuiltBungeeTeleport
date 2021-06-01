package com.eloli.mbt.core.channel.serverPacket;

import com.eloli.sodioncore.channel.ServerPacket;
import com.eloli.sodioncore.channel.util.FieldWrapper;
import com.eloli.sodioncore.channel.util.Priority;

import java.util.List;

public class RequestTeleportPacket extends ServerPacket {
    public static List<FieldWrapper> fieldWrapperList = resolveFieldWrapperList(RequestTeleportPacket.class);

    @Priority(0)
    public String destination;

    public RequestTeleportPacket() {
    }

    public RequestTeleportPacket(String destination) {
        this.destination = destination;
    }

    @Override
    public List<FieldWrapper> getFieldWrapperList() {
        return fieldWrapperList;
    }
}
