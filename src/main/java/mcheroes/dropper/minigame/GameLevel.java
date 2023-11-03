package mcheroes.dropper.minigame;

import org.bukkit.Location;

public class GameLevel {
    private final Location spawn;

    public GameLevel(Location spawn) {
        this.spawn = spawn;
    }

    public Location getSpawn() {
        return spawn;
    }
}
