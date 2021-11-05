package com.darkemerald78.lightrock.blocks;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModBlocks {
    @GameRegistry.ObjectHolder("lightrock:firstblock")
    public static FirstBlock firstBlock;

    @GameRegistry.ObjectHolder("lightrock:destinationblock")
    public static DestinationBlock destinationBlock;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        firstBlock.initModel();
        destinationBlock.initModel();
    }
}
