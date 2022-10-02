package com.darkemerald78.lightrock.network;

import com.darkemerald78.lightrock.LightRock;
import com.darkemerald78.lightrock.blocks.DestinationBlockTileEntity;
import com.darkemerald78.lightrock.blocks.FirstBlockTileEntity;
import com.darkemerald78.lightrock.blocks.TeleportMap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SetTagMessage implements IMessage {

    private String tag;
    private int x,y,z;

    public SetTagMessage() {}

    public SetTagMessage(String tag, int x, int y, int z) {
        this.tag = tag;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getTag() {
        return tag;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.tag = ByteBufUtils.readUTF8String(buf);
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, tag);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }

    public static class Handler implements IMessageHandler<SetTagMessage, IMessage> {
        @Override
        public IMessage onMessage(SetTagMessage message, MessageContext ctx) {
            World w = LightRock.proxy.getWorld(ctx);
            TileEntity te = w.getTileEntity(new BlockPos(message.getX(), message.getY(), message.getZ()));
            TeleportMap tm = TeleportMap.get(w);

            if(te instanceof FirstBlockTileEntity) {
                if(!tm.getFirstBlockMap().containsKey(message.tag)) {
                    ((FirstBlockTileEntity) te).setTag(message.getTag());
                    tm.updateFirstBlockMap(message.getTag(), new BlockPos(message.getX(), message.getY(), message.getZ()));
                } else {
                    EntityPlayer player = LightRock.proxy.getPlayer(ctx);
                    player.sendMessage(new TextComponentString(TextFormatting.RED + "Error: label "+message.getTag()+" is already in use for an arrival block!"));
                }

            } else if(te instanceof DestinationBlockTileEntity) {
                if(!tm.getDestBlockMap().containsKey(message.tag)) {
                    ((DestinationBlockTileEntity) te).setTag(message.getTag());
                    tm.updateDestBlockMap(message.getTag(), new BlockPos(message.getX(), message.getY(), message.getZ()));
                } else {
                    EntityPlayer player = LightRock.proxy.getPlayer(ctx);
                    player.sendMessage(new TextComponentString(TextFormatting.RED + "Error: label "+message.getTag()+" is already in use for a destination block!"));
                }
            }

            return null;
        }
    }
}
