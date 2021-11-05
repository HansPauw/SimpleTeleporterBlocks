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
    private Map<Integer, FirstBlockTileEntity> firstBlockMap = new LinkedHashMap<>();
    private Map<Integer, DestinationBlockTileEntity> destBlockMap = new LinkedHashMap<>();
    private Map<FirstBlockTileEntity, DestinationBlockTileEntity> teleportMap = new LinkedHashMap<>();

    public TeleportMap() {
        super(LightRock.MODID+"_teleportMap");
    }


    public TeleportMap(String name) {
        super(name);
    }

    public Map<Integer, FirstBlockTileEntity> getFirstBlockMap() {
        return firstBlockMap;
    }

    public void setFirstBlockMap(Map<Integer, FirstBlockTileEntity> firstBlockMap) {
        this.firstBlockMap = firstBlockMap;
    }

    public Map<Integer, DestinationBlockTileEntity> getDestBlockMap() {
        return destBlockMap;
    }

    public void setDestBlockMap(Map<Integer, DestinationBlockTileEntity> destBlockMap) {
        this.destBlockMap = destBlockMap;
    }

    public Map<FirstBlockTileEntity, DestinationBlockTileEntity> getTeleportMap() {
        return teleportMap;
    }

    public void setTeleportMap(Map<FirstBlockTileEntity, DestinationBlockTileEntity> teleportMap) {
        this.teleportMap = teleportMap;
    }

    public void updateFirstBlockMap(int i, FirstBlockTileEntity te) {
        firstBlockMap.put(i, te);
        markDirty();
    }

    public Integer getIndexByValue(TileEntity te) {
        Integer result = 0;

        if(te == null) {
            return result;
        }

        if(te instanceof FirstBlockTileEntity) {
            for(int i : firstBlockMap.keySet()) {
                if(firstBlockMap.get(i).equals(te)) {
                    result = i;
                }
            }
        } else if(te instanceof DestinationBlockTileEntity) {
            for(int i : destBlockMap.keySet()) {
                if(destBlockMap.get(i).equals(te)) {
                    result = i;
                }
            }
        }

        return result;

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

    public void updateDestBlockMap(int i, DestinationBlockTileEntity te) {
        destBlockMap.put(i, te);
        markDirty();
    }

    public void updateTeleportMap(FirstBlockTileEntity firstBlockTileEntity, DestinationBlockTileEntity te) {
        teleportMap.put(firstBlockTileEntity, te);
        markDirty();
    }


    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        /*String firstJson = nbt.getString("firstMap");
        String destJson = nbt.getString("destMap");
        String teleJson = nbt.getString("teleMap");

        Gson gson = new Gson();

        this.firstBlockMap = gson.fromJson(firstJson, HashMap.class);
        this.firstBlockMap = gson.fromJson(destJson, HashMap.class);
        this.firstBlockMap = gson.fromJson(teleJson, HashMap.class);*/
        /*for(String s : nbt.getKeySet()) {
            System.out.println(s);
        }*/

       // NBTTagCompound firstBlocks = nbt.getCompoundTag("firstblockMap");
        NBTTagList firstBlocks = nbt.getTagList("firstblockMap", Constants.NBT.TAG_COMPOUND);

        for(int i = 0; i < firstBlocks.tagCount(); i++) {
            NBTTagCompound block = (NBTTagCompound) firstBlocks.getCompoundTagAt(i);
            System.out.println(block.toString());
            int[] pos = block.getIntArray(Integer.toString(i));
            FirstBlockTileEntity TE = new FirstBlockTileEntity();
            TE.setPosition(new BlockPos(pos[0], pos[1], pos[2]));
            firstBlockMap.put(i, TE);
        }

        NBTTagList destBlocks = nbt.getTagList("destBlockMap", Constants.NBT.TAG_COMPOUND);

        for(int i = 0; i < destBlocks.tagCount(); i++) {
            NBTTagCompound block = (NBTTagCompound) destBlocks.getCompoundTagAt(i);
            System.out.println(block.toString());
            int[] pos = block.getIntArray(Integer.toString(i));
            DestinationBlockTileEntity TE = new DestinationBlockTileEntity();
            TE.setPosition(new BlockPos(pos[0], pos[1], pos[2]));
            destBlockMap.put(i, TE);
        }

        if(destBlockMap.size() > 0) {
            for(int i = 0; i < destBlockMap.size(); i++) {
                teleportMap.put(firstBlockMap.get(i), destBlockMap.get(i));
            }
        }

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setInteger("firstBlockEntries", firstBlockMap.size());
        compound.setInteger("destBlockEntries", destBlockMap.size());
        compound.setInteger("teleBlockEntries", teleportMap.size());

        NBTTagList firstBlockMapData = new NBTTagList();


        for(int i = 0; i < firstBlockMap.size(); i++) {
            NBTTagCompound thisFirstBlockData = new NBTTagCompound();
            //thisFirstBlockData.setByteArray(Integer.toString(i), toByteArray(firstBlockMap.get(i)));
            BlockPos pos = firstBlockMap.get(i).getPosition();
            int[] coordList = {pos.getX(), pos.getY(), pos.getZ()};
            thisFirstBlockData.setIntArray(Integer.toString(i), coordList);
            this.firstBlockMap.get(i).writeToNBT(thisFirstBlockData);
            firstBlockMapData.appendTag(thisFirstBlockData);
        }

        compound.setTag("firstblockMap", firstBlockMapData);

        NBTTagList destBlockData = new NBTTagList();

        for(int i = 0; i < destBlockMap.size(); i++) {
            NBTTagCompound destBlockMapData = new NBTTagCompound();
            //destBlockMapData.setByteArray(Integer.toString(i), toByteArray(destBlockMap.get(i)));
            BlockPos pos = destBlockMap.get(i).getPosition();
            int[] coordList = {pos.getX(), pos.getY(), pos.getZ()};
            destBlockMapData.setIntArray(Integer.toString(i), coordList);
            this.destBlockMap.get(i).writeToNBT(destBlockMapData);
            destBlockData.appendTag(destBlockMapData);
        }

        compound.setTag("destBlockMap", destBlockData);

        /*NBTTagList teleData = new NBTTagList();

        for(Map.Entry<FirstBlockTileEntity, DestinationBlockTileEntity> entry : teleportMap.entrySet()) {
            NBTTagCompound teleMapData = new NBTTagCompound();
            //teleMapData.setByteArray(Integer.toString(i), toByteArray(teleportMap.get(i)));
            BlockPos firstPos = teleportMap.get(entry.getKey()).getPosition();
            BlockPos destPos;
            for(DestinationBlockTileEntity dest : teleportMap.values()) {
                if(dest == entry.getValue()) {
                    destPos = dest.getPosition();
                }
            }
            int[] coordList = {firstPos.getX(), firstPos.getY(), firstPos.getZ()};
            teleMapData.setIntArray(Integer.toString(i), coordList);
            this.teleportMap.get(i).writeToNBT(teleMapData);
            destBlockData.appendTag(teleMapData);
        }

        compound.setTag("teleBlockMap", teleData);*/


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
