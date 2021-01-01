package xyz.sculas.fateapi.scenario.listener;

import net.fateuhc.plugin.scenarios.Scenario;
import net.fateuhc.plugin.states.GameState;
import org.bukkit.event.Listener;
import xyz.sculas.fateapi.scenario.ScenarioManager;

public class ScenarioListener implements Listener {

    private final ScenarioManager scenarioManager;

    /**
     * Register a listener for a scenario
     * @param scenarioManager the scenario manager
     * @param scenarioName the name of a scenario this listener is mapped to
     */
    public ScenarioListener(ScenarioManager scenarioManager, String scenarioName) {
        this(scenarioManager, scenarioManager.raw().getByName(scenarioName));
    }

    /**
     * Register a listener for a scenario
     * @param scenarioManager the scenario manager
     * @param scenario the scenario this listener is mapped to
     */
    public ScenarioListener(ScenarioManager scenarioManager, Scenario scenario) {
        this.scenarioManager = scenarioManager;
        if(scenario == null) throw new IllegalArgumentException("That scenario does not exist");
        scenarioManager.register(scenario, this);
    }

    public boolean isGameRunning() {
        return scenarioManager.raw().getPlugin().getGameManager().getGameState() != GameState.values()[0];
    }

}
