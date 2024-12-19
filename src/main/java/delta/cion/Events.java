package delta.cion;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Objects;

public class Events implements Listener {

    private static final ChatBlock p = ChatBlock.getInstance();
    private static final int cooldown = p.getConfig().getInt("Cooldown");

    private static final DataParser dataParser = new DataParser();

    private static BukkitRunnable timer;

    private static final HashMap<String, Integer> timers = new HashMap<>();

    private boolean tGetPlayerBelow = false;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String pname = event.getPlayer().getName();
        Sender.debug(pname+" Has been join");

        if (dataParser.checkPlayer(pname)) return;
        else chatTimer(event.getPlayer().getName());
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        Player pl = event.getPlayer();
        Sender.debug(pl.getName()+" Written on chat");

        if (!dataParser.checkPlayer(pl.getName())) {
            Sender.send((CommandSender) pl, String.format(Objects.requireNonNull(
                    p.getConfig().getString("Messages.Cooldown-MSG")),
                    time()));
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player pl = event.getPlayer();

        boolean playerCheck = dataParser.checkPlayer(pl.getName());
        Sender.debug("dataParser.checkPlayer(pl.getName()) -> "+playerCheck);

        if (!playerCheck) dataParser.setPlayer(pl.getName(), timers.get(pl.getName()));
        if (timers.containsKey(pl.getName())) timer.cancel();
    }

    private void chatTimer(String player) {
        Sender.debug("Timer for "+player+" had been started");
        timer = new BukkitRunnable() {
            public void run() {

                boolean tContainsPlayer = timers.containsKey(player);
                if (tContainsPlayer) tGetPlayerBelow = timers.get(player) <= 0;

                Sender.debug("timers.containsKey(player) -> "+tContainsPlayer);
                Sender.debug("timers.get(player) <= 0 -> "+tGetPlayerBelow);
                if (tContainsPlayer) Sender.debug("Cooldown for "+player+" - "+timers.get(player));

                if (tContainsPlayer && tGetPlayerBelow) {
                    dataParser.setPlayer(player, 0);
                    dataParser.saveDataFile();
                    DataParser.loadData();
                    this.cancel();
                }
                else if (tContainsPlayer) timers.put(player, timers.get(player)-1);
                else timers.put(player, cooldown-dataParser.getPlayer(player));
            }
        };
        timer.runTaskTimer(p, 0L, 20L);
    }

    private static String time() {
        if (cooldown < 60) return getTime(cooldown, "sec");
        else if (cooldown < 3600) return getTime(cooldown / 60, "min");
        else if (cooldown < 86400) return getTime(cooldown / 3600, "hour");
        else return getTime(cooldown / 86400, "day");
    }

    private static String getTime(int time, String s) {
        return time+" "+p.getConfig().getString("Messages."+s);
    }

}
