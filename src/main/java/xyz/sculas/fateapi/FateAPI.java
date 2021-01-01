package xyz.sculas.fateapi;

import lombok.Getter;
import net.fateuhc.plugin.UHC;
import xyz.sculas.fateapi.scenario.ScenarioManager;

public class FateAPI {

    private @Getter final ScenarioManager scenarioManager;

    /**
     * Creates a FateAPI object you can use in your addons.
     * @param fateInstance instance you can grab from {@link UHC#getInstance()}
     * @see UHC#getInstance()
     */
    public FateAPI(UHC fateInstance) {
        this.scenarioManager = new ScenarioManager(fateInstance);
    }

}
