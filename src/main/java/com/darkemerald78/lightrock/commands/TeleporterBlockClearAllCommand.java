package com.darkemerald78.lightrock.commands;

import com.darkemerald78.lightrock.LightRock;
import com.darkemerald78.lightrock.blocks.TeleportMap;
import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class TeleporterBlockClearAllCommand extends CommandBase {
    public TeleporterBlockClearAllCommand(){
        aliases = Lists.newArrayList(LightRock.MODID, "CLEARLABEL", "clearlabel");
    }

    private final List<String> aliases;

    @Override
    @Nonnull
    public String getName() {
        return "clearlabel";
    }

    @Override
    @Nonnull
    public String getUsage(@Nonnull ICommandSender sender) {
        return "clearlabel <tag>";
    }

    @Override
    @Nonnull
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length < 1) {
            return;
        }

        if(sender instanceof EntityPlayer) {
            TeleportMap map = TeleportMap.get(sender.getEntityWorld());
            map.setFirstBlockMap(new LinkedHashMap<>());
            map.setDestBlockMap(new LinkedHashMap<>());
            map.markDirty();

            sender.sendMessage(new TextComponentString(TextFormatting.GRAY + "INFO: Removed all registered teleport blocks"));
        }


    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        if(sender instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) sender;
            if(server.getPlayerList().getOppedPlayers().getEntry(player.getGameProfile()) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return Collections.emptyList();
    }
}
