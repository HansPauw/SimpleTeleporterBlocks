package com.darkemerald78.lightrock;

import com.darkemerald78.lightrock.blocks.*;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
    }

    public void init(FMLInitializationEvent e) {
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new FirstBlock());
        event.getRegistry().register(new DestinationBlock());
        GameRegistry.registerTileEntity(FirstBlockTileEntity.class, LightRock.MODID + "firstblock");
        GameRegistry.registerTileEntity(DestinationBlockTileEntity.class, LightRock.MODID + "destinationblock");
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(ModBlocks.firstBlock).setRegistryName(ModBlocks.firstBlock.getRegistryName()));
        event.getRegistry().register(new ItemBlock(ModBlocks.destinationBlock).setRegistryName(ModBlocks.destinationBlock.getRegistryName()));
    }
}

