package com.eloli.mbt.core;

import com.eloli.mbt.core.channel.clientPacket.DeeperTeleportPacket;
import com.eloli.mbt.core.channel.clientPacket.HelloServerPacket;
import com.eloli.mbt.core.channel.serverPacket.RequestTeleportPacket;
import com.eloli.mbt.core.channel.serverPacket.ShakeTokenPacket;
import com.eloli.mbt.core.config.MainConfiguration;
import com.eloli.sodioncore.channel.BadSignException;
import com.eloli.sodioncore.channel.ClientPacket;
import com.eloli.sodioncore.channel.MessageChannel;
import com.eloli.sodioncore.channel.ServerPacket;
import com.eloli.sodioncore.config.ConfigureService;
import com.eloli.sodioncore.file.BaseFileService;
import com.eloli.sodioncore.logger.AbstractLogger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MbtCore {
    public static BaseFileService fileService;
    public static PlatformAdapter api;
    public static MessageChannel channel;
    public static AbstractLogger logger;
    public static ConfigureService<MainConfiguration> configureService;

    public MbtCore(PlatformAdapter api, AbstractLogger logger, BaseFileService fileService){
        MbtCore.api = api;
        MbtCore.fileService=fileService;
        MbtCore.logger = logger;
        MbtCore.configureService = new ConfigureService<>(fileService,"config.json");
        try {
            configureService.init();
        } catch (Exception e) {
            logger.warn("Failed to load config",e);
        }
        channel = new MessageChannel("mbt:main",
                configureService.instance.serverKey.getBytes(StandardCharsets.UTF_8),
                configureService.instance.clientKey.getBytes(StandardCharsets.UTF_8))
                // Client packets
                .registerClientPacket(HelloServerPacket.class)
                .registerClientPacket(DeeperTeleportPacket.class)
                // Server packets
                .registerServerPacket(ShakeTokenPacket.class)
                .registerServerPacket(RequestTeleportPacket.class);
        api.registerPluginMessageChannel(channel.name);
    }


    // as Bungee
    public ConcurrentMap<UUID, String> serverToken = new ConcurrentHashMap<>();

    // as Sponge + Bukkit
    public ConcurrentMap<UUID, String> clientToken =  new ConcurrentHashMap<>();

    public ConcurrentMap<UUID, String> destination =  new ConcurrentHashMap<>();

    public void onCommand(AbstractPlayer player,String name, String[] command) {
        if(name.equals("mbt")){
            if(player.canMbt()) {
                player.sendClientData(
                        channel.name,
                        channel.getServerFactory(RequestTeleportPacket.class).encode(
                            new RequestTeleportPacket(
                                    command[0]
                            )
                        )
                );
            }else{
                player.sendMessage("Permission denied.");
            }
        }
    }

    public void onSwitchServer(AbstractPlayer player){
        serverToken.remove(player.getUniqueId());
        player.sendServerData(
                channel.name,
                channel.getClientFactory(HelloServerPacket.class).encode(
                        new HelloServerPacket()
                ));
    }
    // as Bukkit
    public boolean onClientMessage(AbstractPlayer player,String channelName, byte[] data) {
        if (channelName.equals(channel.name)) {
            ClientPacket packet;

            try {
                packet = channel.getClientFactory(data).parser(data);
            } catch (BadSignException e) {
                logger.info("Can't parser ClientPacket For "+player.name,e);
                return true;
            }

            if (packet instanceof HelloServerPacket) {
                String proxyToken = Helper.toStringUuid(UUID.randomUUID());
                clientToken.put(player.getUniqueId(), proxyToken);
                player.sendClientData(channel.name,
                        channel.getServerFactory(ShakeTokenPacket.class)
                                .encode(
                                        new ShakeTokenPacket(proxyToken)
                                ));
            } else if (packet instanceof DeeperTeleportPacket) {
                if (clientToken.containsKey(player.getUniqueId())
                        && clientToken.get(player.getUniqueId())
                        .equals(((DeeperTeleportPacket) packet).token)) {
                   String[] destinationPath = ((DeeperTeleportPacket) packet).destination.split("\\.");
                   String[] currentPath = configureService.instance.serverPath.split("\\.");
                    if (destinationPath.length>currentPath.length) {
                        if(!destinationPath[currentPath.length].equals(player.getCurrent())) {
                            player.teleport(destinationPath[currentPath.length]);
                        }
                        if(destinationPath.length != currentPath.length+1) {
                            if(clientToken.containsKey(player.getUniqueId())){
                                ((DeeperTeleportPacket) packet).token = clientToken.get(player.getUniqueId());
                                player.sendClientData(channel.name,
                                        channel.getClientFactory(DeeperTeleportPacket.class).encode(
                                                (DeeperTeleportPacket) packet
                                        ));
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
    public boolean onServerMessage(AbstractPlayer player,String channelName, byte[] data) {
        if (channelName.equals(channel.name)) {
            ServerPacket packet;
            try {
                packet = channel.getServerFactory(data).parser(data);
            } catch (BadSignException e) {
                logger.info("Can't parser ServerPacket For "+player.name,e);
                return true;
            }
            if (packet instanceof ShakeTokenPacket) {
                String token = ((ShakeTokenPacket) packet).token;
                serverToken.put(
                        player.getUniqueId(),
                        token);
                if (destination.containsKey(player.getUniqueId())) {
                    player.sendServerData(channel.name,
                            channel.getClientFactory(DeeperTeleportPacket.class).encode(
                                    new DeeperTeleportPacket(
                                            destination.get(player.getUniqueId()),
                                            token
                                    )
                            ));
                }
            }else if(packet instanceof RequestTeleportPacket){
                String[] destinationPath = ((RequestTeleportPacket) packet).destination.split("\\.");
                String[] currentPath = configureService.instance.serverPath.split("\\.");
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
                    player.sendClientData(channel.name,
                            channel.getServerFactory(RequestTeleportPacket.class).encode(
                                    (RequestTeleportPacket) packet
                            ));
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
}
