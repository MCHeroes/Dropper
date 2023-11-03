package mcheroes.dropper;

import mcheroes.core.api.minigame.Minigame;
import mcheroes.dropper.minigame.GameEventListener;
import mcheroes.dropper.minigame.GameLevel;
import mcheroes.dropper.minigame.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DropperMinigame implements Minigame {
    private final DropperPlugin plugin;
    private final List<GameLevel> levels;
    private final Map<UUID, GamePlayer> players = new HashMap<>();
    private final GameEventListener listener = new GameEventListener();

    private boolean started = false;

    public DropperMinigame(DropperPlugin plugin, List<GameLevel> levels) {
        this.levels = levels;
        this.plugin = plugin;
    }

    @Override
    public String getId() {
        return "dropper";
    }

    @Override
    public void start() {
        started = true;
        Bukkit.getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public void stop() {
        started = false;
        players.clear();
        HandlerList.unregisterAll(listener);
    }

    @Override
    public boolean hasStarted() {
        return started;
    }

    @Override
    public boolean canStart() {
        return !levels.isEmpty() && !started;
    }

    @Override
    public int getMaxSeconds() {
        return 60 * 5; // 5 minutes
    }
}
