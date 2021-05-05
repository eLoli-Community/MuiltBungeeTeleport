package com.eloli.mbt.bukkit;

import com.eloli.mbt.core.MbtCore;
import com.eloli.mbt.core.PlatformAdapter;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitLoader extends JavaPlugin implements PlatformAdapter, Listener {
    public static BukkitLoader instance;
    public static MbtCore core;
    @Override
    public void onEnable() {
        instance = this;
        core = new MbtCore(this,getDataFolder().toString());
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

    @Override
    public void info(String message) {
        getLogger().info(message);
    }

    @Override
    public void info(String message, Exception exception) {
        getLogger().info(message);
        getLogger().info(exception.toString());
    }

    @Override
    public void warn(String message) {
        getLogger().warning(message);
    }

    @Override
    public void warn(String message, Exception exception) {
        getLogger().warning(message);
        getLogger().warning(exception.toString());
    }
}
