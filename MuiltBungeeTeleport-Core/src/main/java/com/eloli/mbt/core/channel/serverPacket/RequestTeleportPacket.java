package com.eloli.mbt.core.channel.serverPacket;

import com.eloli.mbt.core.channel.BadSignException;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class RequestTeleportPacket extends ServerPacket {
    public static final int id = 1;

    public String destination;

    public RequestTeleportPacket(byte[] data) throws BadSignException {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.getInt();
        int destinationLength = buffer.getInt();
        byte[] destinationByte = new byte[destinationLength];
        buffer.get(destinationByte);
        destination=new String(destinationByte, StandardCharsets.UTF_8);
        if (!verify(data)) {
            throw new BadSignException();
        }
    }

    public RequestTeleportPacket(String destination) {
        this.destination=destination;
    }

    @Override
    protected byte[] encode() {
        byte[] destinationByte = destination.getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer = ByteBuffer.allocate(getBytes());
        buffer.putInt(id);
        buffer.putInt(destinationByte.length);
        buffer.put(destinationByte);
        return buffer.array();
    }

    @Override
    public int getSize() {
        return Integer.SIZE // id
                + Integer.SIZE // destination length
                + Byte.SIZE *  destination.getBytes(StandardCharsets.UTF_8).length // destinationByte
                ;
    }
}
