package delta.cion;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DataParser {

    private static final ChatBlock p = ChatBlock.getInstance();

    private static File d;

    private static YamlConfiguration data;

    public DataParser() {
        Sender.debug("DataParser has been enable");
        d = new File(p.getDataFolder(), "saved-players.yml");
        if (!d.exists()) {
            try {
                if (d.createNewFile()) loadData();
            } catch (Exception ignored) {}
        } else loadData();
    }

    public static void loadData() {
        Sender.debug("loadData() has been started");
        d = new File(p.getDataFolder(), "saved-players.yml");
        data = YamlConfiguration.loadConfiguration(d);
    }

    public int getPlayer(String name) {
        return data.getInt(name);
    }

    public void setPlayer(String name, long time) {
        Sender.debug("Player "+name+" has been add on config");
        data.set(name, time);
        saveDataFile();
    }

    public boolean checkPlayer(String name) {
        Sender.debug("data.contains(name) && data.getInt(name) == 0 -> "+(data.getInt(name) == 0));
        return (data.contains(name) && data.getInt(name) == 0);
    }

    public void saveDataFile() {
        try {
            data.save(d);
            Sender.debug("Data file saved (saveDataFile())");
        } catch (Exception ignored) {}
    }
}
