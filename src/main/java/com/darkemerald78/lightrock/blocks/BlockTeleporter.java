package com.darkemerald78.lightrock.blocks;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class BlockTeleporter extends Teleporter {

    private int x,y,z;
    private final WorldServer world;

    public BlockTeleporter(WorldServer worldIn, int x, int y, int z) {
        super(worldIn);
        world = worldIn;
        this.x = x;
        this. y = y;
        this.z = z;
    }

    public BlockPos getPosition() {
        return new BlockPos(x, y, z);
    }

    @Override
    public void placeInPortal(Entity entityIn, float rotationYaw) {
        this.world.getBlockState(new BlockPos(this.x, this.y, this.z));
        entityIn.setPosition(x, y, z);

        entityIn.motionX = 0.0f;
        entityIn.motionY = 0.0f;
        entityIn.motionZ = 0.0f;
    }

    public static void teleportToDimension(EntityPlayer player, int dimension, int x, int y, int z) {
        int oldDimension = player.getEntityWorld().provider.getDimension();
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
        MinecraftServer server = player.getEntityWorld().getMinecraftServer();
        WorldServer worldServer = server.getWorld(dimension);
        player.addExperienceLevel(0);

        if (worldServer == null || worldServer.getMinecraftServer() == null){
            throw new IllegalArgumentException("Dimension: "+dimension+" doesn't exist!");
        }

        worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension(entityPlayerMP, dimension, new BlockTeleporter(worldServer, x, y, z));
        player.setPositionAndUpdate(x, y+1, z);
        if (oldDimension == 1) {
            player.setPositionAndUpdate(x, y+1, z);
            worldServer.spawnEntity(player);
            worldServer.updateEntityWithOptionalForce(player, false);
        }
    }

}
