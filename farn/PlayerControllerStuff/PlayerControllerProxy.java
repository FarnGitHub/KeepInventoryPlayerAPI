package farn.PlayerControllerStuff;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class PlayerControllerProxy extends PlayerControllerSP {
    private PlayerFarn plrKeepInventory;

    public PlayerControllerProxy(Minecraft minecraft1, PlayerFarn plrKeepInb1) {
        super(minecraft1);
        plrKeepInventory = plrKeepInb1;
    }

    public EntityPlayer createPlayer(World world1) {
        EntityPlayerSP newPlayer = new EntityPlayerSP(ModLoader.getMinecraftInstance(), world1, ModLoader.getMinecraftInstance().session, world1.worldProvider.worldType);

        if (ConfigFarn.instance.keepInventory() && ModLoader.getMinecraftInstance().thePlayer != null) {
            // carry over saved NBT
            if (plrKeepInventory.savedInventoryNBT != null) {
                newPlayer.inventory.readFromNBT(plrKeepInventory.savedInventoryNBT);
                plrKeepInventory.savedInventoryNBT = null; // clear so it won’t dupe
            }
        }
        return newPlayer;
    }

    public boolean sendPlaceBlock(EntityPlayer entityPlayer1, World world2, ItemStack itemStack3, int i4, int i5, int i6, int i7) {
        if(ConfigFarn.instance.sneakPlacing()) {
            return (itemStack3 != null && entityPlayer1.isSneaking() ? itemStack3.useItem(entityPlayer1, world2, i4, i5, i6, i7) : super.sendPlaceBlock(entityPlayer1, world2, itemStack3, i4, i5, i6, i7));
        }
        return super.sendPlaceBlock(entityPlayer1, world2, itemStack3, i4, i5, i6, i7);
    }
}
