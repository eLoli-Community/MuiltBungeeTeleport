package com.eloli.mbt.core.channel.clientPacket;

import com.eloli.mbt.core.Config;
import com.eloli.mbt.core.Helper;
import com.eloli.mbt.core.channel.Packet;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public abstract class ClientPacket extends Packet {
    public byte[] pack() {
        byte[] n = encode();
        byte[] m = Helper.merge(n, Config.clientKey.getBytes(StandardCharsets.UTF_8));
        byte[] s = Helper.sha256(m);
        return Helper.merge(m, s);
    }

    protected boolean verify(byte[] r) {
        byte[] n = encode();
        byte[] m = Helper.merge(n, Config.serverKey.getBytes(StandardCharsets.UTF_8));
        byte[] s = Helper.sha256(m);
        return Arrays.equals(Helper.merge(m, s), r);
    }
}
