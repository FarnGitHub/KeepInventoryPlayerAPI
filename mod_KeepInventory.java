package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class mod_KeepInventory extends BaseMod {

    public mod_KeepInventory() {
        if(doesClassExist("mod_SinglePlayerCommands")) {
            System.out.println("KeepInventoryMod: SPC Has been found this mod is disabled now");
        } else {
            PlayerAPI.RegisterPlayerBase(PlayerKeepInventory.class);
        }
    }

    public String Version() {
        return "2.0";
    }

    public static boolean doesClassExist(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
