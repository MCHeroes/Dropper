package mcheroes.dropper.minigame;

import mcheroes.core.CoreProvider;
import mcheroes.core.points.utils.PointsUtil;
import mcheroes.dropper.DropperMinigame;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class GameEventListener implements Listener {
    private final DropperMinigame minigame;

    public GameEventListener(DropperMinigame minigame) {
        this.minigame = minigame;
    }

    @EventHandler
    public void on(EntityDamageEvent event) {
        if (!minigame.hasStarted()) return;
        if (!(event.getEntity() instanceof Player player)) return;

        final GamePlayer data = minigame.getPlayers().get(player.getUniqueId());
        if (data == null) return; // Not an actual player, ignore
        final int currentLevel = data.getCurrentLevel();
        if (currentLevel >= minigame.getLevels().size()) return; // Probably done, ignore
        final GameLevel level = minigame.getLevels().get(currentLevel);
        if (level == null) return; // Shouldn't happen, but let's be safe.

        event.setCancelled(true);
        CoreProvider.get().getScheduler().later(() -> player.teleport(level.getSpawn()), 1L);
    }

    @EventHandler
    public void on(PlayerMoveEvent event) {
        if (!minigame.hasStarted()) return;
        if (!event.hasChangedBlock()) return;
        if (event.getTo().getBlock().getType() != Material.WATER) return;

        final GamePlayer data = minigame.getPlayers().get(event.getPlayer().getUniqueId());
        if (data == null) return; // Not an actual player, ignore

        // Give 5 points
        PointsUtil.give(event.getPlayer().getUniqueId(), 5, CoreProvider.get().getActionManager());
        // If first to complete, give 2 extra points
        if (isFirst(data)) {
            PointsUtil.give(event.getPlayer().getUniqueId(), 2, CoreProvider.get().getActionManager());
        }

        data.setCurrentLevel(data.getCurrentLevel() + 1);

        // If completed, give overall points
        if (data.getCurrentLevel() >= minigame.getLevels().size()) {
            final int give = 160 - (getPlayersWhoCompleted() * 5);
            if (give <= 0) return;

            PointsUtil.give(event.getPlayer().getUniqueId(), give, CoreProvider.get().getActionManager());
        }
    }

    private int getPlayersWhoCompleted() {
        int total = 0;
        for (GamePlayer player : minigame.getPlayers().values()) {
            if (player.getCurrentLevel() >= minigame.getLevels().size()) {
                total++;
            }
        }

        return total;
    }

    private boolean isFirst(GamePlayer us) {
        for (GamePlayer other : minigame.getPlayers().values()) {
            if (other.getCurrentLevel() >= us.getCurrentLevel() + 1) return false;
        }

        return true;
    }
}
