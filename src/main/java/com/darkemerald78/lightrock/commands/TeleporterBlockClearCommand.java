package com.darkemerald78.lightrock.commands;

import com.darkemerald78.lightrock.LightRock;
import com.darkemerald78.lightrock.blocks.BlockTeleporter;
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
import net.minecraftforge.fml.common.FMLCommonHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;


public class TeleporterBlockClearCommand extends CommandBase {

    public TeleporterBlockClearCommand(){
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
        String tag = args[0];
        if(tag != null && !tag.trim().isEmpty()) {
            if(sender instanceof EntityPlayer) {
                TeleportMap tm = TeleportMap.get(server.getWorld(sender.getEntityWorld().provider.getDimension()));

                if(tm.getFirstBlockMap().containsKey(tag) && tm.getDestBlockMap().containsKey(tag)) {
                    for(String s1 : tm.getFirstBlockMap().keySet()) {
                        if(s1.equals(tag)) {
                            tm.getFirstBlockMap().remove(s1);
                        }
                    }

                    for(String s1 : tm.getDestBlockMap().keySet()) {
                        if(s1.equals(tag)) {
                            tm.getDestBlockMap().remove(s1);
                        }
                    }

                    sender.sendMessage(new TextComponentString(TextFormatting.GRAY + "INFO: Removed all registered teleport blocks with label: " + tag));
                } else {
                    sender.sendMessage(new TextComponentString(TextFormatting.RED + "Error: label "+tag+" not found!"));
                }


            }
        } else {
            sender.sendMessage(new TextComponentString(TextFormatting.RED + "Error: please specify a label!"));
        }


    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return Collections.emptyList();
    }

}
