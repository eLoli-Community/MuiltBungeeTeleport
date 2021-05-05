package com.eloli.mbt.core.config;

import com.eloli.mbt.core.Helper;
import com.google.gson.annotations.Expose;

import java.util.UUID;

public class MainConfiguration extends Configure {

    @Migrate("version")
    @Expose(deserialize = false)
    public Integer version = 2;

    @Migrate("serverPath")
    @Lore("The server's path")
    @Expose
    public String serverPath = "main.hello";

    @Migrate("clientKey")
    @Lore("The key use when SodionAuth run as Bungee")
    @Lore("It should as same as Server's serverKey")
    @Expose
    public String clientKey = Helper.toStringUuid(UUID.randomUUID());

    @Migrate("serverKey")
    @Lore("The key use when SodionAuth run as Server")
    @Lore("It should as same as Bungee's clientKey")
    @Lore("If you have a Bungee outside the Bungee")
    @Lore("You should make this as same as outside Bungee's clientKet")
    @Expose
    public String serverKey = Helper.toStringUuid(UUID.randomUUID());
}
