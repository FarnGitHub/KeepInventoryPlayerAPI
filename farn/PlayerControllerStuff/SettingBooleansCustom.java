package farn.PlayerControllerStuff;

import net.minecraft.src.*;

public class SettingBooleansCustom extends SettingBoolean {

    public SettingBooleansCustom(String name, Boolean def) {
        super(name, def);
    }

    public void set(Boolean v, String context) {
        this.values.put(context, v);
        ConfigFarn.instance.nameToBool.replace(this.backendName, v);
        ConfigFarn.instance.saveOptions();

        if(this.displayWidget != null) {
            this.displayWidget.update();
        }

    }

    public Boolean get(String context) {
        return ConfigFarn.instance.nameToBool.getOrDefault(this.backendName, false);
    }



}
