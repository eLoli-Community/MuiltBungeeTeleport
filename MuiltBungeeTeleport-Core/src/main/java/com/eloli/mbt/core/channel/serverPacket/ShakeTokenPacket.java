package com.eloli.mbt.core.channel.serverPacket;

import com.eloli.mbt.core.Helper;
import com.eloli.mbt.core.channel.BadSignException;

import java.nio.ByteBuffer;

public class ShakeTokenPacket extends ServerPacket {
    public static final int id = 0;
    public static final int size =
            Integer.SIZE // id
                    + Character.SIZE * 32 // token
            ;
    public static final int bytes = size / Byte.SIZE;

    public String token;

    public ShakeTokenPacket(byte[] data) throws BadSignException {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        buffer.getInt();
        token = Helper.readBuffer(buffer, 32);
        if (!verify(data)) {
            throw new BadSignException();
        }
    }

    public ShakeTokenPacket(String token) {
        this.token = token;
    }

    @Override
    protected byte[] encode() {
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        buffer.putInt(id);
        Helper.putBuffer(buffer, 32, token);
        return buffer.array();
    }

    @Override
    public int getSize() {
        return size;
    }
}
