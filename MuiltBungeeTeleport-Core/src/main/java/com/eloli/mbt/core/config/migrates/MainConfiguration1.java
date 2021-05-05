package com.eloli.mbt.core.config.migrates;

import com.eloli.mbt.core.Helper;
import com.eloli.mbt.core.config.Configure;
import com.google.gson.annotations.Expose;

import java.util.UUID;

public class MainConfiguration1 extends Configure {

    @Expose(deserialize = false)
    public Integer version = 2;

    @Expose
    public String serverPath = "main.hello";

    @Expose
    public String clientKey = Helper.toStringUuid(UUID.randomUUID());

    @Expose
    public String serverKey = Helper.toStringUuid(UUID.randomUUID());
}
