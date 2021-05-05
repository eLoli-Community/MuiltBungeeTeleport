package com.eloli.mbt.core;

import com.eloli.mbt.core.channel.Channel;
import com.eloli.mbt.core.channel.clientPacket.ClientPacket;
import com.eloli.mbt.core.channel.clientPacket.DeeperTeleportPacket;
import com.eloli.mbt.core.channel.clientPacket.HelloServerPacket;
import com.eloli.mbt.core.channel.serverPacket.RequestTeleportPacket;
import com.eloli.mbt.core.channel.serverPacket.ServerPacket;
import com.eloli.mbt.core.channel.serverPacket.ShakeTokenPacket;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MbtCore {
    public static String basePath;
    public static PlatformAdapter api;
    public MbtCore(PlatformAdapter api, String basePath){
        MbtCore.api = api;
        MbtCore.basePath=basePath;
        try {
            Config.init();
        } catch (IOException e) {
            warn("Failed to load config",e);
        }
        api.registerPluginMessageChannel(Channel.name);
    }


    // as Bungee
    public ConcurrentMap<UUID, String> serverToken = new ConcurrentHashMap<>();

    // as Bukkit
    public ConcurrentMap<UUID, String> clientToken =  new ConcurrentHashMap<>();

    public ConcurrentMap<UUID, String> destination =  new ConcurrentHashMap<>();

    public void onCommand(AbstractPlayer player,String name, String[] command) {
        if(name.equals("mbt")){
            if(player.canMbt()) {
                player.sendClientData(
                        Channel.name,
                        new RequestTeleportPacket(
                                command[0]
                        ).pack());
            }else{
                player.sendMessage("Permission denied.");
            }
        }
    }

    public void onSwitchServer(AbstractPlayer player){
        serverToken.remove(player.getUniqueId());
        player.sendServerData(Channel.name,new HelloServerPacket().pack());
    }
    // as Bukkit
    public boolean onClientMessage(AbstractPlayer player,String channel, byte[] data) {
        if (channel.equals(Channel.name)) {
            ClientPacket packet = Channel.parserClient(data);
            if (packet instanceof HelloServerPacket) {
                String proxyToken = Helper.toStringUuid(UUID.randomUUID());
                clientToken.put(player.getUniqueId(), proxyToken);
                player.sendClientData(Channel.name,
                        new ShakeTokenPacket(proxyToken).pack());
            } else if (packet instanceof DeeperTeleportPacket) {
                if (clientToken.containsKey(player.getUniqueId())
                        && clientToken.get(player.getUniqueId())
                        .equals(((DeeperTeleportPacket) packet).token)) {
                   String[] destinationPath = ((DeeperTeleportPacket) packet).destination.split("\\.");
                   String[] currentPath = Config.serverPath.split("\\.");
                    if (destinationPath.length>currentPath.length) {
                        if(!destinationPath[currentPath.length].equals(player.getCurrent())) {
                            player.teleport(destinationPath[currentPath.length]);
                        }
                        if(destinationPath.length != currentPath.length+1) {
                            if(clientToken.containsKey(player.getUniqueId())){
                                ((DeeperTeleportPacket) packet).token = clientToken.get(player.getUniqueId());
                                player.sendClientData(Channel.name,packet.pack());
                            }else {
                                destination.put(player.getUniqueId(), ((DeeperTeleportPacket) packet).destination);
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    // as Bungee
    public boolean onServerMessage(AbstractPlayer player,String channel, byte[] data) {
        if (channel.equals(Channel.name)) {
            ServerPacket packet = Channel.parserServer(data);
            if (packet instanceof ShakeTokenPacket) {
                String token = ((ShakeTokenPacket) packet).token;
                serverToken.put(
                        player.getUniqueId(),
                        token);
                if (destination.containsKey(player.getUniqueId())) {
                    player.sendServerData(Channel.name,
                            new DeeperTeleportPacket(
                                    destination.get(player.getUniqueId()),
                                    token
                            ).pack());
                }
            }else if(packet instanceof RequestTeleportPacket){
                String[] destinationPath = ((RequestTeleportPacket) packet).destination.split("\\.");
                String[] currentPath = Config.serverPath.split("\\.");
                boolean needBack = false;
                if(destinationPath.length <= currentPath.length){
                    needBack=true;
                }else {
                    for (int i = 0; i < currentPath.length; i++) {
                        if(!destinationPath[i].equals(currentPath[i])){
                            needBack=true;
                            break;
                        }
                    }
                }
                if(needBack){
                    player.sendClientData(Channel.name,packet.pack());
                }else{
                    player.teleport(destinationPath[currentPath.length]);
                    if(destinationPath.length != currentPath.length+1) {
                        destination.put(player.getUniqueId(), ((RequestTeleportPacket) packet).destination);

                    }
                }
            }
            return true;
        }
        return false;
    }

    public void onQuit(AbstractPlayer player) {
        clientToken.remove(player.getUniqueId());
        serverToken.remove(player.getUniqueId());
    }

    public static void info(String message){
        api.info(message);
    }
    public static void info(String message,Exception exception){
        api.info(message,exception);
    }
    public static void warn(String message){
        api.warn(message);
    }
    public static void warn(String message,Exception exception){
        api.warn(message,exception);
    }
}
