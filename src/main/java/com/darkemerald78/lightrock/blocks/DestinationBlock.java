package com.darkemerald78.lightrock.blocks;

import com.darkemerald78.lightrock.CommonProxy;
import com.darkemerald78.lightrock.LightRock;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Map;

public class DestinationBlock extends Block implements ITileEntityProvider {
    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i) {

        return new DestinationBlockTileEntity();
    }

    public DestinationBlock() {
        super(Material.ROCK);
        setLightLevel(15/16f);
        setUnlocalizedName(LightRock.MODID+".destinationblock");
        setRegistryName("destinationblock");
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "destinationblock"));
    }

    @Override
    public boolean hasTileEntity(IBlockState p_hasTileEntity_1_) {
        return true;
    }

    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase entity, ItemStack itemStack) {
        if(!world.isRemote) {
            getTE(world, pos).setPosition(pos);
            TeleportMap map = TeleportMap.get(world);
            map.updateDestBlockMap(map.getDestBlockMap().size(), getTE(world, pos));
            //map.markDirty();
            for(Map.Entry<Integer, FirstBlockTileEntity> firstMap : map.getFirstBlockMap().entrySet()) {
                for(Map.Entry<Integer, DestinationBlockTileEntity> destMap : map.getDestBlockMap().entrySet()) {
                    if(firstMap.getKey() == destMap.getKey()) {
                        map.updateTeleportMap(firstMap.getValue(), destMap.getValue());
                        //map.markDirty();
                    }
                }
            }
        }

    }


    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            TeleportMap tmap = TeleportMap.get(world);
            for(Map.Entry<FirstBlockTileEntity, DestinationBlockTileEntity> map : tmap.getTeleportMap().entrySet()) {
                if(map.getValue().getPosition().equals(pos)) {
                    player.setPositionAndUpdate(map.getKey().getPosition().getX(), map.getKey().getPosition().getY()+1, map.getKey().getPosition().getZ());
                }
            }
        }
        return true;
    }

    protected DestinationBlockTileEntity getTE(World world, BlockPos pos) {
        return (DestinationBlockTileEntity) world.getTileEntity(pos);
    }
}
