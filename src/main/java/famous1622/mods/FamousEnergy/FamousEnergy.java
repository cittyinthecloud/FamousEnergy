package famous1622.mods.FamousEnergy;

import famous1622.mods.FamousEnergy.block.ModBlocks;
import famous1622.mods.FamousEnergy.tileentities.ModTileEntities;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = FamousEnergy.MODID, name = FamousEnergy.MODNAME, version = FamousEnergy.VERSION)
public class FamousEnergy {
	public static final String MODID = "famousenergy";
	public static final String MODNAME = "FamousEnergy";
	public static final String VERSION = "0.0.1";
	@Instance
	public static FamousEnergy instance = new FamousEnergy();
	
	 
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
	
	}
	
	@EventHandler
	public void init(FMLInitializationEvent e) {
		ModBlocks.init();
		ModTileEntities.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
	
	}
}
