package xyz.sculas.fateapi.scenario.listener;

import net.fateuhc.plugin.UHC;
import net.fateuhc.plugin.events.ScenarioToggleEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import xyz.sculas.fateapi.scenario.ScenarioManager;

public class ScenarioToggleListener implements Listener {

    private final ScenarioManager scenarioManager;

    public ScenarioToggleListener(ScenarioManager scenarioManager) {
        this.scenarioManager = scenarioManager;
        // Currently this registers using the UHC plugin, meaning even if the extension disables this
        // listener will still run. This could be changed the extension's plugin, but this works too.
        Bukkit.getPluginManager().registerEvents(this, UHC.getInstance());
    }

    @EventHandler
    public void scenarioToggle(ScenarioToggleEvent event) {
        Listener listener = scenarioManager.getListener(event.getScenario());
        if(event.isToEnable()) {
            // Scenario is active
            Bukkit.getPluginManager().registerEvents(listener, UHC.getInstance());
        } else {
            // Scenario is inactive
            HandlerList.unregisterAll(listener);
        }
    }

}
