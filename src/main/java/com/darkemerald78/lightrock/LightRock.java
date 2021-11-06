package com.darkemerald78.lightrock;


import com.darkemerald78.lightrock.blocks.DestinationBlock;
import com.darkemerald78.lightrock.blocks.FirstBlock;
import com.darkemerald78.lightrock.commands.TeleporterBlockClearCommand;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.lang.ref.Reference;


@Mod(modid = LightRock.MODID, name = LightRock.MODNAME, version = LightRock.MODVERSION, dependencies = "required-after:forge@[11.16.0.1865,)", useMetadata = true)
public class LightRock {
    public static final String MODID = "lightrock";
    public static final String MODNAME = "LightRock";
    public static final String MODVERSION= "0.0.1";

    @SidedProxy(clientSide = "com.darkemerald78.lightrock.ClientProxy", serverSide = "com.darkemerald78.lightrock.ServerProxy")
    public static CommonProxy proxy;

    public static FirstBlock firstblock = new FirstBlock();
    public static ItemBlock firstBlockItem = new ItemBlock(firstblock);

    public static DestinationBlock destBlock = new DestinationBlock();
    public static ItemBlock destBlockItem = new ItemBlock(destBlock);

    public static final SimpleNetworkWrapper NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

    @Mod.Instance
    public static LightRock instance;

    //public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        //logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        event.registerServerCommand(new TeleporterBlockClearCommand());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}

