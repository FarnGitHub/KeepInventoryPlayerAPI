package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class mod_KeepInventory extends BaseMod {

    public mod_KeepInventory() {
        PlayerAPI.RegisterPlayerBase(PlayerKeepInventory.class);
    }

    public String Version() {
        return "yes";
    }
}
