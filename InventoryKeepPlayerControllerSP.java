package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class InventoryKeepPlayerControllerSP extends PlayerControllerSP {
    private PlayerKeepInventory plrKeepInventory;

    public InventoryKeepPlayerControllerSP(Minecraft minecraft1, PlayerKeepInventory plrKeepInb1) {
        super(minecraft1);
        plrKeepInventory = plrKeepInb1;
    }

    public EntityPlayer createPlayer(World world1) {
        EntityPlayerSP newPlayer = new EntityPlayerSP(ModLoader.getMinecraftInstance(), world1, ModLoader.getMinecraftInstance().session, world1.worldProvider.worldType);

        if (ModLoader.getMinecraftInstance().thePlayer != null) {
            // carry over saved NBT
            if (plrKeepInventory.savedInventoryNBT != null) {
                newPlayer.inventory.readFromNBT(plrKeepInventory.savedInventoryNBT);
                plrKeepInventory.savedInventoryNBT = null; // clear so it won’t dupe
            }
        }
        return newPlayer;
    }
}
