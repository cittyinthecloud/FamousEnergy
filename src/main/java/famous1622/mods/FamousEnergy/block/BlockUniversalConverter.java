package famous1622.mods.FamousEnergy.block;

import famous1622.mods.FamousEnergy.tileentities.ConverterTE;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockUniversalConverter extends BlockContainer {

	public BlockUniversalConverter(String unlocalizedName, String regName, Material materialIn) {
		super(materialIn);
		this.setUnlocalizedName(unlocalizedName);
		this.setRegistryName(regName);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		this.setHarvestLevel("pickaxe", 0);
		this.setSoundType(SoundType.METAL);
		this.setLightLevel(1.0F);
		this.isBlockContainer = true;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new ConverterTE();
	}
	
    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        super.breakBlock(world, pos, state);
        world.removeTileEntity(pos);
    }
    
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
}
