package com.darkemerald78.lightrock.blocks;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

import java.io.Serializable;

public class DestinationBlockTileEntity extends TileEntity implements Serializable {

    private BlockPos position;

    private FirstBlock firstBlock;

    public BlockPos getPosition() {
        return position;
    }

    public void setPosition(BlockPos position) {
        this.position = position;
        markDirty();
    }

    public FirstBlock getFirstBlock() {
        return firstBlock;
    }

    public void setFirstBlock(FirstBlock firstBlock) {
        this.firstBlock = firstBlock;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        int x = compound.getInteger("x");
        int y = compound.getInteger("y");
        int z = compound.getInteger("z");

        position = new BlockPos(x, y, z);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("x", position.getX());
        compound.setInteger("y", position.getY());
        compound.setInteger("z", position.getZ());

        return compound;
    }
}
