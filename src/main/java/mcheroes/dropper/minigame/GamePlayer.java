package mcheroes.dropper.minigame;

import java.util.UUID;

public class GamePlayer {
    private final UUID uniqueId;
    private int currentLevel;

    public GamePlayer(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public boolean isDone(int maxLevels) {
        return currentLevel > maxLevels;
    }
}
