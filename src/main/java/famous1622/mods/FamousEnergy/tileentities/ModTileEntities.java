package famous1622.mods.FamousEnergy.tileentities;

import net.minecraftforge.fml.common.registry.GameRegistry;

public final class ModTileEntities {
	public static void init(){
		GameRegistry.registerTileEntity(ConverterTE.class, "famousenergyconverter");
	}
}
