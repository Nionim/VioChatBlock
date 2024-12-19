package delta.cion;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class ChatBlock extends JavaPlugin {

    private Events eventsListener;

    public static ChatBlock getInstance() {
        return getPlugin(ChatBlock.class);
    }

    @Override
    public void onEnable() {
        eventsListener = new Events();
        setListeners();
        saveDefaultConfig();
        Objects.requireNonNull(getCommand("cbreload")).setExecutor(new ReloadCommand());
    }

    public void setListeners() {
        if (isListenerRegistered()) HandlerList.unregisterAll(eventsListener);
        getServer().getPluginManager().registerEvents(eventsListener, this);
    }

    public boolean isListenerRegistered() {
        return HandlerList.getRegisteredListeners(this).stream()
                .anyMatch(listener -> listener.getListener() == eventsListener);
    }
}
