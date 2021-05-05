package com.eloli.mbt.core.channel.clientPacket;

import com.eloli.mbt.core.channel.BadSignException;

import java.nio.ByteBuffer;

public class HelloServerPacket extends ClientPacket {
    public static final int id = 0;
    public static final int size =
            Integer.SIZE // id
            ;
    public static final int bytes = size / Byte.SIZE;

    public HelloServerPacket(byte[] data) throws BadSignException {
        if (!verify(data)) {
            throw new BadSignException();
        }
    }

    public HelloServerPacket() {

    }

    @Override
    protected byte[] encode() {
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        buffer.putInt(id);
        return buffer.array();
    }

    @Override
    public int getSize() {
        return size;
    }
}
