package famous1622.mods.FamousEnergy.energy;

import cofh.api.energy.IEnergyStorage;
import net.darkhax.tesla.api.implementation.BaseTeslaContainer;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class RFTeslaEStorage extends BaseTeslaContainer implements IEnergyStorage, ITeslaConsumer, ITeslaProducer, ITeslaHolder, INBTSerializable<NBTTagCompound> {

	protected int energy;
	protected int capacity;
	protected int maxReceive;
	protected int maxExtract;

	public RFTeslaEStorage() {
		this(8192);
	}
	
	public RFTeslaEStorage(NBTTagCompound dataTag){
		this.deserializeNBT(dataTag);
	}
	public RFTeslaEStorage(int capacity) {

		this(capacity, capacity, capacity);
	}

	public RFTeslaEStorage(int capacity, int maxTransfer) {

		this(capacity, maxTransfer, maxTransfer);
	}

	public RFTeslaEStorage(int capacity, int maxReceive, int maxExtract) {

		this.capacity = capacity;
		this.maxReceive = maxReceive;
		this.maxExtract = maxExtract;
	}

	public RFTeslaEStorage readFromNBT(NBTTagCompound nbt) {

		this.energy = nbt.getInteger("Energy");

		if (energy > capacity) {
			energy = capacity;
		}
		return this;
	}

	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {

		if (energy < 0) {
			energy = 0;
		}
		nbt.setInteger("Energy", energy);
		return nbt;
	}

	public RFTeslaEStorage setCapacity(int capacity) {

		this.capacity = capacity;

		if (energy > capacity) {
			energy = capacity;
		}
		return this;
	}

	public RFTeslaEStorage setMaxTransfer(int maxTransfer) {

		setMaxReceive(maxTransfer);
		setMaxExtract(maxTransfer);
		return this;
	}

	public RFTeslaEStorage setMaxReceive(int maxReceive) {

		this.maxReceive = maxReceive;
		return this;
	}

	public RFTeslaEStorage setMaxExtract(int maxExtract) {

		this.maxExtract = maxExtract;
		return this;
	}

	public int getMaxReceive() {

		return maxReceive;
	}

	public int getMaxExtract() {

		return maxExtract;
	}

	/**
	 * This function is included to allow for server to client sync. Do not call this externally to the containing Tile Entity, as not all IEnergyHandlers
	 * are guaranteed to have it.
	 *
	 * @param energy
	 */
	public void setEnergyStored(int energy) {

		this.energy = energy;

		if (this.energy > capacity) {
			this.energy = capacity;
		} else if (this.energy < 0) {
			this.energy = 0;
		}
	}
	

	/**
	 * This function is included to allow the containing tile to directly and efficiently modify the energy contained in the EnergyStorage. Do not rely on this
	 * externally, as not all IEnergyHandlers are guaranteed to have it.
	 *
	 * @param energy
	 */
	public void modifyEnergyStored(int energy) {

		this.energy += energy;

		if (this.energy > capacity) {
			this.energy = capacity;
		} else if (this.energy < 0) {
			this.energy = 0;
		}
	}

	/* IEnergyStorage */
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate) {

		int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));

		if (!simulate) {
			energy += energyReceived;
		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(int maxExtract, boolean simulate) {
		//System.out.println("maxExtract:"+maxExtract+" simulate:"+simulate);
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));

		if (!simulate) {
			energy -= energyExtracted;
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored() {

		return energy;
	}

	@Override
	public int getMaxEnergyStored() {

		return capacity;
	}
	
	
	//Tesla API 
	
	@Override
	public NBTTagCompound serializeNBT() {
        final NBTTagCompound dataTag = new NBTTagCompound();
        dataTag.setLong("TeslaPower", this.energy);
        dataTag.setLong("TeslaCapacity", this.capacity);
        dataTag.setLong("TeslaInput", this.maxReceive);
        dataTag.setLong("TeslaOutput", this.maxExtract);
        return dataTag;
	}
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
        this.energy = (int) nbt.getLong("TeslaPower");
        
        if (nbt.hasKey("TeslaCapacity"))
            this.capacity = (int) nbt.getLong("TeslaCapacity");
            
        if (nbt.hasKey("TeslaInput"))
            this.maxReceive = (int) nbt.getLong("TeslaInput");
            
        if (nbt.hasKey("TeslaOutput"))
            this.maxExtract = (int) nbt.getLong("TeslaOutput");
            
        if (this.energy > this.getCapacity())
            this.energy = (int) this.getCapacity();
    }
	@Override
	public long getStoredPower() {
		return this.getEnergyStored();
	}
	@Override
	public long getCapacity() {
		return this.getMaxEnergyStored();
	}
	@Override
	public long takePower(long power, boolean simulated) {
		return this.extractEnergy((int) power, simulated);
	}
	@Override
	public long givePower(long power, boolean simulated) {
		return this.receiveEnergy((int) power, simulated);
	}
	
	@Override
	public BaseTeslaContainer setCapacity(long capacity) {
		this.setCapacity((int) capacity);
		return this;
	}
	
	@Override
	public long getInputRate() {
		return this.getMaxReceive();
	}
	
	@Override
	public long getOutputRate() {
		return this.getMaxExtract();
	}
	
	@Override
	public BaseTeslaContainer setInputRate(long rate) {
		this.setMaxReceive((int) rate);
		return this;
	}
	
	@Override
	public BaseTeslaContainer setOutputRate(long rate) {
		this.setMaxExtract((int) rate);
		return this;
	}
	
	@Override
	public BaseTeslaContainer setTransferRate(long rate) {
		this.setMaxTransfer((int) rate);
		return this;
	}

	public void setEnergy(int i) {
		this.setEnergyStored(i);
	}
}

