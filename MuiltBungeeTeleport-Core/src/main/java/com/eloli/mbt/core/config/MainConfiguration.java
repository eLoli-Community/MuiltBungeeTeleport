package com.eloli.mbt.core.config;

import com.eloli.mbt.core.Helper;
import com.eloli.sodioncore.config.Configure;
import com.google.gson.annotations.Expose;

import java.util.UUID;

public class MainConfiguration extends Configure {

    @Expose(deserialize = false)
    public Integer version = 0;

    @Expose
    public String serverPath = "main.hello";

    @Expose
    public String clientKey = Helper.toStringUuid(UUID.randomUUID());

    @Expose
    public String serverKey = Helper.toStringUuid(UUID.randomUUID());
}
