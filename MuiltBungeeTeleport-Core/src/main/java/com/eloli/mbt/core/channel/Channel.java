package com.eloli.mbt.core.channel;

import com.eloli.mbt.core.MbtCore;
import com.eloli.mbt.core.channel.clientPacket.ClientPacket;
import com.eloli.mbt.core.channel.clientPacket.HelloServerPacket;
import com.eloli.mbt.core.channel.clientPacket.DeeperTeleportPacket;
import com.eloli.mbt.core.channel.serverPacket.RequestTeleportPacket;
import com.eloli.mbt.core.channel.serverPacket.ServerPacket;
import com.eloli.mbt.core.channel.serverPacket.ShakeTokenPacket;
import com.google.common.collect.ImmutableMap;

import java.nio.ByteBuffer;
import java.util.Map;

public class Channel {
    public static final String name = "mbt:main";
    public static final Map<Integer, Class<? extends ClientPacket>> clientPackets =
            new ImmutableMap.Builder<Integer, Class<? extends ClientPacket>>()
                    .put(0, HelloServerPacket.class)
                    .put(1, DeeperTeleportPacket.class)
                    .build();
    public static final Map<Integer, Class<? extends ServerPacket>> serverPackets =
            new ImmutableMap.Builder<Integer, Class<? extends ServerPacket>>()
                    .put(0, ShakeTokenPacket.class)
                    .put(1, RequestTeleportPacket.class)
                    .build();

    public static ClientPacket parserClient(byte[] data) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            Class<? extends ClientPacket> clazz = clientPackets.get(buffer.getInt());
            return clazz.getConstructor(byte[].class).newInstance((Object) data);
        } catch (Exception e) {
            MbtCore.warn("Failed to parser clientPacket",e);
            return null;
        }
    }

    public static ServerPacket parserServer(byte[] data) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(data);
            Class<? extends ServerPacket> clazz = serverPackets.get(buffer.getInt());
            return clazz.getConstructor(byte[].class).newInstance((Object) data);
        } catch (Exception e) {
            MbtCore.warn("Failed to parser serverPacket",e);
            return null;
        }
    }
}
