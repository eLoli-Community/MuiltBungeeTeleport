package com.eloli.mbt.core.channel.serverPacket;

import com.eloli.sodioncore.channel.ServerPacket;
import com.eloli.sodioncore.channel.util.FieldWrapper;

import java.util.List;

public class ShakeTokenPacket extends ServerPacket {
    public static List<FieldWrapper> fieldWrapperList = resolveFieldWrapperList(ShakeTokenPacket.class);
    public String token;

    public ShakeTokenPacket() {
    }

    public ShakeTokenPacket(String token) {
        this.token = token;
    }

    @Override
    public List<FieldWrapper> getFieldWrapperList() {
        return null;
    }
}
