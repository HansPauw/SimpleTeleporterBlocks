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
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Map;

public class FirstBlock extends Block implements ITileEntityProvider {


    public FirstBlock() {
        super(Material.ROCK);
        setLightLevel(15/16f);
        setUnlocalizedName(LightRock.MODID+".firstblock");
        setRegistryName("firstblock");
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);

    }


    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "firstblock"));
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
            map.updateFirstBlockMap(map.getFirstBlockMap().size(), getTE(world, pos));
            //map.markDirty();
        }

    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState blockState, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!world.isRemote) {
            TeleportMap tmap = TeleportMap.get(world);
            for(Map.Entry<FirstBlockTileEntity, DestinationBlockTileEntity> map : tmap.getTeleportMap().entrySet()) {
                if(map.getKey().getPosition().equals(pos)) {
                    player.setPositionAndUpdate(map.getValue().getPosition().getX(), map.getValue().getPosition().getY()+1, map.getValue().getPosition().getZ());
                }
            }
        }

        return true;
    }

    protected FirstBlockTileEntity getTE(World world, BlockPos pos) {
        return (FirstBlockTileEntity) world.getTileEntity(pos);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int i) {
        return new FirstBlockTileEntity();
    }
}
