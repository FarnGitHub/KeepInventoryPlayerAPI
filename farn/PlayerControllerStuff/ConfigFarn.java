package farn.PlayerControllerStuff;

import net.minecraft.client.Minecraft;
import net.minecraft.src.WidgetBoolean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigFarn {

    private final Properties prop = new Properties();
    public File cfgFile;

    public static final ConfigFarn instance = new ConfigFarn();

    public final HashMap<String, Boolean> nameToBool = new HashMap<String, Boolean>();
    public final HashMap<String, String> optionText = new HashMap<String, String>();

    private ConfigFarn() {
        cfgFile = new File(Minecraft.getMinecraftDir(), "playerControllerStuff.cfg");
        this.addSetting("KeepInventory", "Keep Inventory", true);
        this.addSetting("SneakPlacing", "Sneak Placing", true);
        loadOption();
    }

    public void saveOptions() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(cfgFile))) {
            for (Map.Entry<String, Boolean> entry : nameToBool.entrySet()) {
                writer.println(entry.getKey() + "=" + entry.getValue());
            }
        } catch (Exception e) {
            System.out.println("Failed to save cfg");
            e.printStackTrace();
        }

    }

    public void loadOption() {
        if (!cfgFile.exists()) {
            saveOptions();
            return;
        }

        try (FileInputStream in = new FileInputStream(cfgFile)) {
            prop.load(in);
            for (String key : nameToBool.keySet()) {
                String value = prop.getProperty(key, String.valueOf(nameToBool.get(key)));
                nameToBool.put(key, Boolean.parseBoolean(value));
            }
        } catch (Exception e) {
            System.out.println("Failed to load cfg");
            e.printStackTrace();
        }
    }

    private void addSetting(String mapName, String normalName, boolean defValue) {
        nameToBool.put(mapName, defValue);
        optionText.put(mapName, normalName);
    }

    public boolean keepInventory() {
        return nameToBool.get("KeepInventory");
    }

    public boolean sneakPlacing() {
        return nameToBool.get("SneakPlacing");
    }
}
