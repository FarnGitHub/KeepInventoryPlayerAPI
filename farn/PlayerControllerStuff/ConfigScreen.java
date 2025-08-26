package farn.PlayerControllerStuff;

import net.minecraft.src.*;

import java.util.Map;

public class ConfigScreen extends ModSettingScreen {

    public ConfigScreen(String name) {
        super(name);
        for (Map.Entry<String, Boolean> entry : ConfigFarn.instance.nameToBool.entrySet()) {
            addSetting(entry.getKey(), entry.getValue());
        }
    }

    private void addSetting(String mapName, boolean defValue) {
        WidgetBoolean widget = new WidgetBoolean(new SettingBooleansCustom(mapName, defValue), ConfigFarn.instance.optionText.get(mapName), "ON", "OFF");
        this.append(widget);
    }

}
