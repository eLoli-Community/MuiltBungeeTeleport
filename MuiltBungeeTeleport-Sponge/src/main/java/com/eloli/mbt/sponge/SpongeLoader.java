package com.eloli.mbt.sponge;

import com.eloli.mbt.core.MbtCore;
import com.eloli.mbt.core.PlatformAdapter;
import com.eloli.sodioncore.file.BaseFileService;
import com.eloli.sodioncore.logger.AbstractLogger;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.network.PlayerConnection;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

@Plugin(id = "muiltbungeeteleport",
        dependencies = {
            @Dependency(id = "sodioncore"),
        }
)
public class SpongeLoader implements PlatformAdapter {
    public static SpongeLoader instance;
    public static MbtCore core;
    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        instance = this;
        core = new MbtCore(this, new AbstractLogger() {
            @Override
            public void info(String info) {
                logger.info(info);
            }

            @Override
            public void info(String info, Exception exception) {
                logger.info(info,exception);
            }

            @Override
            public void warn(String info) {
                logger.warn(info);
            }

            @Override
            public void warn(String info, Exception exception) {
                logger.warn(info,exception);
            }
        }, new BaseFileService(Sponge.getConfigManager().getPluginConfig(this).getConfigPath().getParent().toString()));
        Sponge.getCommandManager().register(this,
                CommandSpec.builder()
                        .description(Text.of("Muilt bungee teleport")).permission("mbt.tp")
                        .arguments(GenericArguments.string(Text.of("dest")))
                        .executor((src, args) -> {
                            if (src instanceof Player) {
                                core.onCommand(new SpongePlayer((Player)src), "mbt",
                                        args.getAll("dest").toArray(new String[]{}));
                            }
                            return CommandResult.success();
                        }).build()
                ,"mbt");
    }

    public void onQuit(ClientConnectionEvent.Disconnect event) {
        core.onQuit(new SpongePlayer(event.getTargetEntity()));
    }

    @Override
    public void registerPluginMessageChannel(String pChannel) {
        Sponge.getChannelRegistrar().getOrCreateRaw(this,pChannel).addListener((data, connection, side) -> {
            if(connection instanceof PlayerConnection
                    && side.isServer()) {
                core.onClientMessage(new SpongePlayer(((PlayerConnection) connection).getPlayer()), pChannel, data.readBytes(data.available()));
            }
        });
    }
}
