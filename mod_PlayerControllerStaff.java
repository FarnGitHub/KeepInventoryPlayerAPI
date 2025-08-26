package net.minecraft.src;

import farn.PlayerControllerStuff.ConfigFarn;
import farn.PlayerControllerStuff.ConfigScreen;
import farn.PlayerControllerStuff.PlayerFarn;

public class mod_PlayerControllerStaff extends BaseMod {

    public mod_PlayerControllerStaff() {
        if(doesClassExist("mod_SinglePlayerCommands")) {
            System.out.println("KeepInventoryMod: SPC Has been found this mod is disabled now");
        } else {
            PlayerAPI.RegisterPlayerBase(PlayerFarn.class);
            if (doesClassExist("net.minecraft.src.ModSettingScreen") || doesClassExist("ModSettingScreen")) {
                new ConfigScreen("PlayerControllerStaff");
            }
        }
    }

    public String Version() {
        return "3.0";
    }

    public static boolean doesClassExist(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static int getPlayerScoreValue(EntityPlayer plr) {
        return plr.scoreValue;
    }

    public static void setUnusedFlag(EntityPlayer plr) {
        plr.unused_flag = true;
    }

    public static void dropFewItems(EntityPlayer plr) {
        plr.dropFewItems();
    }

    public static void setSize(EntityPlayer plr) {
        plr.setSize(0.2F, 0.2F);
    }
}
