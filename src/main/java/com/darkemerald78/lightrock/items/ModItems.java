package com.darkemerald78.lightrock.items;

import com.darkemerald78.lightrock.LightRock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModItems {
    @GameRegistry.ObjectHolder(LightRock.MODID+":wrench")
    public static WrenchItem wrenchItem;

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        wrenchItem.initModel();
    }
}
