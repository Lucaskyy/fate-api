package xyz.sculas.fateapi.example;

import net.fateuhc.plugin.UHC;
import net.fateuhc.plugin.scenarios.Scenario;
import org.bukkit.Material;
import xyz.sculas.fateapi.FateAPI;

public class MyScenario {

    public void onEnable() {
        FateAPI fateAPI = new FateAPI(UHC.getInstance());
        Scenario scenario = fateAPI.getScenarioManager().create(
                "My Scenario!",
                Material.BEDROCK,
                false,
                "Some very",
                "cool description!"
        );
        fateAPI.getScenarioManager().add(scenario);
        new MyScenarioListener(fateAPI.getScenarioManager(), scenario);
    }

}
