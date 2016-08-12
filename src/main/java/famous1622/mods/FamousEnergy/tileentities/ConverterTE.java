package famous1622.mods.FamousEnergy.tileentities;


import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import famous1622.mods.FamousEnergy.energy.RFTeslaEStorage;
import ic2.api.energy.event.EnergyTileLoadEvent;
import ic2.api.energy.event.EnergyTileUnloadEvent;
import ic2.api.energy.tile.IEnergyAcceptor;
import ic2.api.energy.tile.IEnergyEmitter;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.info.Info;
import ic2.api.tile.IEnergyStorage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;

public class ConverterTE extends TileEntity implements ITickable, IEnergyHandler, IEnergyProvider, IEnergyReceiver, IEnergyStorage, IEnergySink, IEnergySource {
	RFTeslaEStorage storage = new RFTeslaEStorage();
	private boolean addedToEnet;
	public int[] powerCooldown = {0,0,0,0,0,0};

	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		//System.out.println("Read Converter from nbt");
		
		super.readFromNBT(nbt);
		storage=new RFTeslaEStorage(nbt);
		this.updateBlocks();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		//System.out.println("Write Converter to nbt");
		super.writeToNBT(nbt);
		NBTTagCompound dataTag = storage.serializeNBT();
		this.updateBlocks();
		return dataTag;
	}
	/* IEnergyConnection */
	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return true;
	}
	

	/* IEnergyReceiver */
	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		this.markDirty();
		this.powerCooldown[from.getIndex()] = 2;
		return storage.receiveEnergy(maxReceive, simulate);
	}

	/* IEnergyProvider */
	@Override
	public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
		this.markDirty();
		return storage.extractEnergy(maxExtract, simulate);
	}

	/* IEnergyHandler */
	@Override
	public int getEnergyStored(EnumFacing from) {

		return storage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {

		return storage.getMaxEnergyStored();
	}

	//IC2
	
	@Override
	public int getStored() {
		return storage.getEnergyStored()/4;
	}


	@Override
	public void setStored(int energy) {
		storage.setEnergy(energy*4);
	}


	@Override
	public int addEnergy(int amount) {
		if (amount>0){
			storage.receiveEnergy(amount*4, false);
			this.updateBlocks();
			return this.getStored();	
		}
		//System.out.println("IC2 Extract");
		storage.extractEnergy(Math.abs(amount)*4, false);
		this.updateBlocks();
		return this.getStored();
	}


	@Override
	public int getCapacity() {
		return storage.getMaxEnergyStored()/4;
	}


	@Override
	public int getOutput() {
		return (int) this.getOutputEnergyUnitsPerTick();
	}


	@Override
	public double getOutputEnergyUnitsPerTick() {
		return 2048;
	}


	@Override
	public boolean isTeleporterCompatible(EnumFacing side) {
		return true;
	}

	@Override
	public boolean acceptsEnergyFrom(IEnergyEmitter emitter, EnumFacing side) {
		return true;
	}

	@Override
	public boolean emitsEnergyTo(IEnergyAcceptor receiver, EnumFacing side) {
		return true;
	}

	@Override
	public double getOfferedEnergy() {
		return Math.min(this.getStored(), this.getOutputEnergyUnitsPerTick());
	}

	@Override
	public void drawEnergy(double amount) {
		this.addEnergy(Math.negateExact((int) amount));
	}

	@Override
	public int getSourceTier() {
		return 10;
	}

	@Override
	public double getDemandedEnergy() {
		return this.getCapacity()-this.getStored();
	}

	@Override
	public int getSinkTier() {
		return 10;
	}

	public int getSpace() {
		return this.getCapacity() - this.getStored();
	}
	@Override
	public double injectEnergy(EnumFacing directionFrom, double amount, double voltage) {
		int accepted = (int) Math.min(this.getSpace(), amount);
		this.addEnergy(accepted);
		return amount-accepted;
	}

	private void powerPush() {
		for (EnumFacing face : EnumFacing.VALUES) {
			if (this.powerCooldown[face.getIndex()]!=0){
				continue;
			}
			BlockPos updatepos = this.pos.offset(face);	
			TileEntity TE = this.getWorld().getTileEntity(updatepos);
			if (TE != null){
				//System.out.println("TE on face "+face+" is"+TE);
				if (TE instanceof IEnergyReceiver) {
					//System.out.println(TE+" is an IEnergyReceiver");
					IEnergyReceiver receiver = (IEnergyReceiver) TE;
					storage.modifyEnergyStored(Math.negateExact(receiver.receiveEnergy(face.getOpposite(), this.getEnergyStored(face), false)));
				} else {
					//System.out.println(TE+" is not an IEnergyReceiver :(");
				}
			} else{
				//System.out.println("No TE on face "+ face);
			}
		}
	}
	private void updateBlocks(){
		for (EnumFacing face : EnumFacing.VALUES) {
			BlockPos updatepos = this.pos.offset(face);
			IBlockState iblockstate = this.getWorld().getBlockState(updatepos);
			Chunk chunk = this.getWorld().getChunkFromBlockCoords(updatepos);
			int flags = this.getWorld().getBlockState(updatepos).getBlock().getMetaFromState(iblockstate);
			this.getWorld().markAndNotifyBlock(updatepos, chunk, iblockstate, iblockstate, flags);		
		}
	}
	
	private void updateFace(EnumFacing face){
		BlockPos updatepos = this.pos.offset(face);
		IBlockState iblockstate = this.getWorld().getBlockState(updatepos);
		Chunk chunk = this.getWorld().getChunkFromBlockCoords(updatepos);
		int flags = this.getWorld().getBlockState(updatepos).getBlock().getMetaFromState(iblockstate);
		this.getWorld().markAndNotifyBlock(updatepos, chunk, iblockstate, iblockstate, flags);		
	}
	@Override
	public void update() {
		if (!addedToEnet) onLoaded();
		this.powerPush();
		for (int i = 0; i < powerCooldown.length; i++) {
			if (powerCooldown[i]>0) {
				powerCooldown[i]--;
			}
		}
	}
	
	public void onLoaded() {
		if (!addedToEnet &&	!this.getWorld().isRemote &&	Info.isIc2Available()) {
		worldObj = this.getWorld();
		pos = this.getPos();
		MinecraftForge.EVENT_BUS.post(new EnergyTileLoadEvent(this));
		addedToEnet = true;
	}
}
	@Override
	public void invalidate() {
		super.invalidate();
		onChunkUnload();
	}

	@Override
	public void onChunkUnload() {
		if (addedToEnet && Info.isIc2Available()) {
			MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));

			addedToEnet = false;
		}
	}

	@Override
	public void onLoad() {
		onLoaded();
		super.onLoad();
	}
}
