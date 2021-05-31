package com.eloli.mbt.bukkit;

import com.eloli.mbt.core.MbtCore;
import com.eloli.mbt.core.PlatformAdapter;
import com.eloli.sodioncore.file.BaseFileService;
import com.eloli.sodioncore.logger.AbstractLogger;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLoader extends JavaPlugin implements PlatformAdapter, Listener {
    public static BukkitLoader instance;
    public static MbtCore core;
    @Override
    public void onEnable() {
        instance = this;
        core = new MbtCore(this, new AbstractLogger() {
            @Override
            public void info(String info) {
                getLogger().info(info);
            }

            @Override
            public void info(String info, Exception exception) {
                getLogger().info(info);
                exception.printStackTrace();
            }

            @Override
            public void warn(String info) {
                getLogger().warning(info);
            }

            @Override
            public void warn(String info, Exception exception) {
                getLogger().warning(info);
                exception.printStackTrace();
            }
        },new BaseFileService(getDataFolder().toString()));
        getServer().getPluginManager().registerEvents(this,this);
        getCommand("mbt").setExecutor((sender, command, label, args) -> {
            if(sender instanceof Player) {
                core.onCommand(new BukkitPlayer((Player) sender),command.getName(), args);
            }
            return true;
        });
    }

    public void onQuit(PlayerQuitEvent event){
        core.onQuit(new BukkitPlayer(event.getPlayer()));
    }

    @Override
    public void registerPluginMessageChannel(String pChannel) {
        getServer().getMessenger()
                .registerIncomingPluginChannel(this, pChannel, (channel, player, message) -> {
                    core.onClientMessage(new BukkitPlayer(player),channel,message);
                });
        getServer().getMessenger()
                .registerOutgoingPluginChannel(this, pChannel);
    }
}
