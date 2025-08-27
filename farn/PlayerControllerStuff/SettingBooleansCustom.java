package farn.PlayerControllerStuff;

import net.minecraft.src.*;

public class SettingBooleansCustom extends SettingBoolean {
    //instead of super class's backEndName we used our own name for compatibilitty with older GuiAPI
    private String nameFarn = "nan";

    public SettingBooleansCustom(String name, Boolean def) {
        super(name, def);
        this.nameFarn = name;
    }

    public void set(Boolean v, String context) {
        this.values.put(context, v);
        ConfigFarn.instance.nameToBool.replace(this.nameFarn, v);
        ConfigFarn.instance.saveOptions();
    }

    public Boolean get(String context) {
        return ConfigFarn.instance.nameToBool.getOrDefault(this.nameFarn, false);
    }

}
