package com.eloli.mbt.core.channel;

public abstract class Packet {
    protected abstract byte[] encode();

    public abstract byte[] pack();

    protected abstract boolean verify(byte[] s);

    public abstract int getSize();

    public int getBytes() {
        return getSize() / Byte.SIZE;
    }
}
