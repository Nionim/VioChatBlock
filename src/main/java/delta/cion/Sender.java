package delta.cion;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Sender {

    private static final String PREFIX = "&5&lViolette &8&l~ &7&lCB &8&l>>&r ";

    private static String translateColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(translateColor(PREFIX+message));
    }

    public static void debug(String message) {
        boolean debugMode = ChatBlock.getInstance().getConfig().getBoolean("Debug-Mode");
        if (debugMode) Bukkit.getConsoleSender().sendMessage(translateColor(PREFIX+message+" &f(&eDEBUG&f)"));
    }
}
