package com.darkemerald78.lightrock.blocks;

import com.darkemerald78.lightrock.LightRock;
import com.google.gson.Gson;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import scala.Int;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TeleportMap extends WorldSavedData implements Serializable {
    private Map<String, BlockPos> firstBlockMap = new LinkedHashMap<>();
    private Map<String, BlockPos> destBlockMap = new LinkedHashMap<>();

    private Map<BlockTeleporter, BlockTeleporter> teleportMap = new LinkedHashMap<>();

    public TeleportMap() {
        super(LightRock.MODID+"_teleportMap");
    }


    public TeleportMap(String name) {
        super(name);
    }


    public Map<String, BlockPos> getFirstBlockMap() {
        return firstBlockMap;
    }

    public void setFirstBlockMap(Map<String, BlockPos> firstBlockMap) {
        this.firstBlockMap = firstBlockMap;
    }

    public Map<String, BlockPos> getDestBlockMap() {
        return destBlockMap;
    }

    public void setDestBlockMap(Map<String, BlockPos> destBlockMap) {
        this.destBlockMap = destBlockMap;
    }

    public Map<BlockTeleporter, BlockTeleporter> getTeleportMap() {
        return teleportMap;
    }

    public void setTeleportMap(Map<BlockTeleporter, BlockTeleporter> teleportMap) {
        this.teleportMap = teleportMap;
    }

    public void updateFirstBlockMap(String te, BlockPos pos) {
        firstBlockMap.put(te, pos);
        markDirty();
    }


    public void deleteFirstBlockMapEntry(Integer i) {
        firstBlockMap.entrySet().removeIf(e -> e.getKey().equals(i));
        markDirty();
    }

    public void removeTeleportMapArrivalPoint(FirstBlockTileEntity te) {
        teleportMap.keySet().removeIf(e -> e.equals(te));
        markDirty();
    }

    public void deleteDestBlockMapEntry(Integer i) {
        destBlockMap.entrySet().removeIf(e -> e.getKey().equals(i));
        markDirty();
    }

    public void removeTeleportMapDestinationPoint(DestinationBlockTileEntity te) {
        teleportMap.remove(te);
        markDirty();
    }

    public void updateDestBlockMap(String te, BlockPos pos) {
        destBlockMap.put(te, pos);
        markDirty();
    }

    public void updateTeleportMap(BlockTeleporter t1, BlockTeleporter t2) {
        this.teleportMap.put(t1, t2);
    }



    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        NBTTagList firstBlocks = nbt.getTagList("firstblockMap", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < firstBlocks.tagCount(); i++) {

            NBTTagCompound firstBlock = (NBTTagCompound) firstBlocks.getCompoundTagAt(i);
            System.out.println(firstBlock.toString());
            String tag = "";
            for (String s : firstBlock.getKeySet()) {
                tag = s;
            }

            int[] pos = firstBlock.getIntArray(tag);
            BlockPos bpos = new BlockPos(pos[0], pos[1], pos[2]);
            firstBlockMap.put(tag, bpos);

        }

        NBTTagList destBlocks = nbt.getTagList("destblockMap", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < destBlocks.tagCount(); i++) {

            NBTTagCompound destBlock = (NBTTagCompound) destBlocks.getCompoundTagAt(i);
            System.out.println(destBlock.toString());
            String tag = "";
            for (String s : destBlock.getKeySet()) {
                tag = s;
            }

            int[] pos = destBlock.getIntArray(tag);
            BlockPos bpos = new BlockPos(pos[0], pos[1], pos[2]);
            destBlockMap.put(tag, bpos);

        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("firstBlockEntries", firstBlockMap.size());
        compound.setInteger("destBlockEntries", destBlockMap.size());
        compound.setInteger("teleBlockEntries", teleportMap.size());


        NBTTagList firstBlockMapData = new NBTTagList();

        for(Map.Entry<String, BlockPos> map : firstBlockMap.entrySet() ) {
            NBTTagCompound thisFirstBlockData = new NBTTagCompound();
            BlockPos pos = map.getValue();
            int[] coordList = {pos.getX(), pos.getY(), pos.getZ()};
            thisFirstBlockData.setIntArray(map.getKey(), coordList);
            firstBlockMapData.appendTag(thisFirstBlockData);
        }

        compound.setTag("firstblockMap", firstBlockMapData);

        NBTTagList destBlockMapData = new NBTTagList();

        for(Map.Entry<String, BlockPos> map : destBlockMap.entrySet() ) {
            NBTTagCompound thisDestBlockData = new NBTTagCompound();
            BlockPos pos = map.getValue();
            int[] coordList = {pos.getX(), pos.getY(), pos.getZ()};
            thisDestBlockData.setIntArray(map.getKey(), coordList);
            destBlockMapData.appendTag(thisDestBlockData);
        }

        compound.setTag("destblockMap", destBlockMapData);


    return compound;
    }

    public static TeleportMap get(World w) {
        MapStorage s = w.getMapStorage();
        TeleportMap d = (TeleportMap) s.getOrLoadData(TeleportMap.class, LightRock.MODID+"_teleportMap");

        if(d == null) {
            d = new TeleportMap();
            s.setData(LightRock.MODID+"_teleportMap", d);
        }

        return d;
    }

    public void save(World w) {
        w.getMapStorage().setData(LightRock.MODID+"_teleportMap", this);
    }
}
