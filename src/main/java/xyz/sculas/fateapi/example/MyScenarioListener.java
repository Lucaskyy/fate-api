package xyz.sculas.fateapi.example;

import net.fateuhc.plugin.scenarios.Scenario;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import xyz.sculas.fateapi.scenario.ScenarioManager;
import xyz.sculas.fateapi.scenario.listener.ScenarioListener;

public class MyScenarioListener extends ScenarioListener {

    public MyScenarioListener(ScenarioManager scenarioManager, Scenario scenario) {
        super(scenarioManager, scenario);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        //TODO: This only runs when the scenario is active!
        event.getPlayer().sendMessage("You moved!");
    }

}
