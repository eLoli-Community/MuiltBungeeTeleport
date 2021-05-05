package com.eloli.mbt.core.channel.clientPacket;

import com.eloli.mbt.core.Helper;
import com.eloli.mbt.core.channel.BadSignException;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class DeeperTeleportPacket extends ClientPacket {
    public static final int id = 1;

    public String destination;
    public String token;

    public DeeperTeleportPacket(byte[] data) throws BadSignException {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.getInt();
        int destinationLength = buffer.getInt();
        byte[] destinationByte = new byte[destinationLength];
        buffer.get(destinationByte);
        destination=new String(destinationByte, StandardCharsets.UTF_8);
        token = Helper.readBuffer(buffer, 32);
        if (!verify(data)) {
            throw new BadSignException();
        }
    }

    public DeeperTeleportPacket(String destination,String token) {
        this.destination=destination;
        this.token = token;
    }

    @Override
    protected byte[] encode() {
        byte[] destinationByte = destination.getBytes(StandardCharsets.UTF_8);

        ByteBuffer buffer = ByteBuffer.allocate(getBytes());
        buffer.putInt(id);
        buffer.putInt(destinationByte.length);
        buffer.put(destinationByte);
        Helper.putBuffer(buffer, 32, token);
        return buffer.array();
    }

    @Override
    public int getSize() {
        return Integer.SIZE // id
                + Integer.SIZE // destination length
                + Byte.SIZE *  destination.getBytes(StandardCharsets.UTF_8).length // destinationByte
                + Character.SIZE * 32 // token
                ;
    }
}
