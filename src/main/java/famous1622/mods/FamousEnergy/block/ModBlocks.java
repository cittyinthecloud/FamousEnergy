package famous1622.mods.FamousEnergy.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModBlocks {
	public static Block uniConvert = new BlockUniversalConverter("uniConvert", "converter", Material.PISTON);
	public static final void init(){
		GameRegistry.register(uniConvert);
		ItemBlock uniConItem = new ItemBlock(uniConvert);
		uniConItem.setRegistryName(uniConvert.getRegistryName());
		GameRegistry.register(uniConItem);
	}
	
	public static void registerRenders(){
	    registerRender(uniConvert);
	}
	
	private static void registerRender(Block block){
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
	}
}
