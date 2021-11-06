package com.darkemerald78.lightrock;

import com.darkemerald78.lightrock.blocks.*;
import com.darkemerald78.lightrock.gui.TagGUIHandler;
import com.darkemerald78.lightrock.items.ModItems;
import com.darkemerald78.lightrock.items.WrenchItem;
import com.darkemerald78.lightrock.network.SetTagMessage;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
    }

    public void init(FMLInitializationEvent e) {
        NetworkRegistry.INSTANCE.registerGuiHandler(LightRock.instance, new TagGUIHandler());
        LightRock.NETWORK.registerMessage(SetTagMessage.Handler.class, SetTagMessage.class, 0, Side.CLIENT);
        LightRock.NETWORK.registerMessage(SetTagMessage.Handler.class, SetTagMessage.class, 0, Side.SERVER);
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
        event.getRegistry().register(new WrenchItem());
    }

    public World getWorld(MessageContext ctx)
    {
        if(ctx.side.isClient())
            return Minecraft.getMinecraft().world;
        else
            return ctx.getServerHandler().player.world;
    }
}

