package com.eloli.mbt.core.channel.clientPacket;

import com.eloli.sodioncore.channel.ClientPacket;
import com.eloli.sodioncore.channel.util.FieldWrapper;

import java.util.List;

public class HelloServerPacket extends ClientPacket {
    public static List<FieldWrapper> fieldWrapperList = resolveFieldWrapperList(HelloServerPacket.class);

    public HelloServerPacket() {
    }

    @Override
    public List<FieldWrapper> getFieldWrapperList() {
        return fieldWrapperList;
    }
}
