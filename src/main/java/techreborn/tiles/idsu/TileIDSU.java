package techreborn.tiles.idsu;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.StringUtils;

import reborncore.common.misc.Functions;
import techreborn.config.ConfigTechReborn;
import techreborn.init.ModBlocks;
import techreborn.powerSystem.TilePowerAcceptor;

public class TileIDSU extends TilePowerAcceptor {

    public String ownerUdid;

    @Override
    public double getEnergy() {
        if (ownerUdid == null && StringUtils.isBlank(ownerUdid) || StringUtils.isEmpty(ownerUdid)) {
            return 0.0;
        }
        IDSUManager.IDSUValueSaveData data = IDSUManager.INSTANCE.getSaveDataForWorld(worldObj, ownerUdid);
        if (data == null) {
            return 0;
        }
        return data.storedPower;
    }

    @Override
    public void setEnergy(double energy) {
        if (ownerUdid == null && StringUtils.isBlank(ownerUdid) || StringUtils.isEmpty(ownerUdid)) {
            return;
        }
        IDSUManager.IDSUValueSaveData data = IDSUManager.INSTANCE.getSaveDataForWorld(worldObj, ownerUdid);
        if (data == null) {
            return;
        }
        data.storedPower = energy;
    }

    @Override
    public void readFromNBTWithoutCoords(NBTTagCompound tag) {

    }

    @Override
    public void writeToNBTWithoutCoords(NBTTagCompound tag) {

    }

    @Override
    public double getMaxPower() {
        return 1000000000;
    }

    @Override
    public boolean canAcceptEnergy(ForgeDirection direction) {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) != Functions.getIntDirFromDirection(direction);
    }

    @Override
    public boolean canProvideEnergy(ForgeDirection direction) {
        return worldObj.getBlockMetadata(xCoord, yCoord, zCoord) == Functions.getIntDirFromDirection(direction);
    }

    @Override
    public double getMaxOutput() {
        return output;
    }

    @Override
    public double getMaxInput() {
        return maxStorage;
    }

    public int tier;
    public int output;
    public double maxStorage;
    private double euLastTick = 0;
    private double euChange;
    private int ticks;

    public TileIDSU(int tier1, int output1, int maxStorage1) {
        super(tier1);
        this.tier = tier1;
        this.output = output1;
        this.maxStorage = maxStorage1;
    }

    public TileIDSU() {
        this(5, 2048, 100000000);
    }

    public float getChargeLevel() {
        float ret = (float) this.getEnergy() / (float) this.maxStorage;
        if (ret > 1.0F) {
            ret = 1.0F;
        }

        return ret;
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.ownerUdid = nbttagcompound.getString("ownerUdid");
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        if (ownerUdid == null && StringUtils.isBlank(ownerUdid) || StringUtils.isEmpty(ownerUdid)) {
            return;
        }
        nbttagcompound.setString("ownerUdid", this.ownerUdid);
    }

    public void updateEntity() {
        super.updateEntity();

        if (ticks == ConfigTechReborn.aveargeEuOutTickTime) {
            euChange = -1;
            ticks = 0;

        } else {
            ticks++;
            euChange += getEnergy() - euLastTick;
            if (euLastTick == getEnergy()) {
                euChange = 0;
            }
        }

        euLastTick = getEnergy();

        boolean needsInvUpdate = false;

        if (needsInvUpdate) {
            this.markDirty();
        }

    }

    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side) {
        return false;
    }

    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        NBTTagCompound tileEntity = new NBTTagCompound();
        ItemStack dropStack = new ItemStack(ModBlocks.Idsu, 1);
        writeToNBT(tileEntity);
        dropStack.setTagCompound(new NBTTagCompound());
        dropStack.stackTagCompound.setTag("tileEntity", tileEntity);
        return dropStack;
    }

    public double getEuChange() {
        if (euChange == -1) {
            return -1;
        }
        return (euChange / ticks);
    }

    public void handleGuiInputFromClient(int id) {
        if (id == 0) {
            output += 256;
        }
        if (id == 1) {
            output += 64;
        }
        if (id == 2) {
            output -= 64;
        }
        if (id == 3) {
            output -= 256;
        }
        if (output > 4096) {
            output = 4096;
        }
        if (output <= -1) {
            output = 0;
        }
    }
}
