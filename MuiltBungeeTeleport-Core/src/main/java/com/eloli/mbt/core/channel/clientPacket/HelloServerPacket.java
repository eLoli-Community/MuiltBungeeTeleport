package com.eloli.mbt.core.channel.clientPacket;

import com.eloli.mbt.core.channel.serverPacket.ShakeTokenPacket;
import com.eloli.sodioncore.channel.ClientPacket;
import com.eloli.sodioncore.channel.util.FieldWrapper;

import java.nio.ByteBuffer;
import java.util.List;

public class HelloServerPacket extends ClientPacket {
    public static List<FieldWrapper> fieldWrapperList = resolveFieldWrapperList(ShakeTokenPacket.class);

    public HelloServerPacket(){}

    @Override
    public List<FieldWrapper> getFieldWrapperList() {
        return null;
    }
}
