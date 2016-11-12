package famous1622.mods.FamousEnergy;

import famous1622.mods.FamousEnergy.block.ModBlocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModRecipes {

	private static ShapedOreRecipe uniConvertRecipe;

	public static void init() {
		uniConvertRecipe = new ShapedOreRecipe(new ItemStack(ModBlocks.uniConvert), "WWW","WGW","WWW", 'W', "plankWood", 'G', Items.GOLD_INGOT);
		GameRegistry.addRecipe(uniConvertRecipe);
	}

}
