package delta.cion;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private static final ChatBlock p = ChatBlock.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command c, @NotNull String ss, @NotNull String[] sss) {
        if (s.hasPermission("cb.reload")) {
            p.reloadConfig();
            p.setListeners();
            DataParser.loadData();
            Sender.send(s, p.getConfig().getString("Messages.Reload-Success"));
            return true;
        }
        Sender.send(s, p.getConfig().getString("Messages.No-Permissions"));
        return true;
    }
}
