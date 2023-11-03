package mcheroes.dropper;

import mcheroes.core.CoreProvider;
import mcheroes.core.minigames.actions.RegisterMinigameAction;
import mcheroes.dropper.minigame.GameLevel;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class DropperPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();

        final List<GameLevel> levels = new ArrayList<>();
        getConfig().getConfigurationSection("levels").getValues(false).forEach((id, data) ->
                levels.add(new GameLevel(ConfigParser.parsePosition((ConfigurationSection) data))));

        CoreProvider.get().getActionManager().run(new RegisterMinigameAction(new DropperMinigame(this, levels)));
    }
}
