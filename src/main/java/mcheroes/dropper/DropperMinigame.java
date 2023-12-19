package mcheroes.dropper;

import com.google.common.collect.ImmutableList;
import mcheroes.core.CoreProvider;
import mcheroes.core.api.minigame.Minigame;
import mcheroes.core.minigames.utils.MinigameIntroducer;
import mcheroes.core.teams.Team;
import mcheroes.core.teams.utils.TeamUtil;
import mcheroes.dropper.minigame.GameEventListener;
import mcheroes.dropper.minigame.GameLevel;
import mcheroes.dropper.minigame.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DropperMinigame implements Minigame {
    private final DropperPlugin plugin;
    private final ImmutableList<GameLevel> levels;
    private final Map<UUID, GamePlayer> players = new HashMap<>();
    private final GameEventListener listener = new GameEventListener(this);
    private final Location hubSpawn;

    private boolean started = false;

    public DropperMinigame(DropperPlugin plugin, List<GameLevel> levels, Location hubSpawn) {
        this.levels = ImmutableList.copyOf(levels);
        this.plugin = plugin;
        this.hubSpawn = hubSpawn;
    }

    @Override
    public String getId() {
        return "dropper";
    }

    @Override
    public void start() {
        started = true;
        for (Player player : Bukkit.getOnlinePlayers()) {
            final Team team = TeamUtil.getTeam(CoreProvider.get().getActionManager(), player);
            if (team == null) continue;

            players.put(player.getUniqueId(), new GamePlayer());
        }
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

    @Override
    public void handlePreGame(Player player) {
        player.teleport(hubSpawn);
    }

    @Override
    public MinigameIntroducer getIntroducer() {
        return new MinigameIntroducer(7, List.of(
                "<green>Welcome to Dropper!",
                "<green>Dropper is a game that tests your agility and movement skills.",
                "<green>Your goal is to complete all 15 levels before the time ends. You must jump into the water and dodge all the obstacles.",
                "<green>You get 5 points for completing a level, 2 points extra for completing a level first and 160 points for completing the course, decreasing by 5 per player completion.",
                "<green>You will be teleported shortly, good luck!"
        ), 5);
    }

    public ImmutableList<GameLevel> getLevels() {
        return levels;
    }

    public Map<UUID, GamePlayer> getPlayers() {
        return players;
    }
}
