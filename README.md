# FateAPI 
FateAPI is a scenario API made for [FateUHC](https://www.mc-market.org/resources/9081/)<br>
Currently this utilizes [Reflection](https://www.oracle.com/technical-resources/articles/java/javareflection.html) to create the scenarios.
This is a temporary solution till the Developer of FateUHC has finished their API, and this is also a contribution to FateUHC to help the Developer.

## Usage
You may find more detailed examples [here.](https://github.com/Lucaskyy/fate-api/tree/main/src/main/java/xyz/sculas/fateapi/example)

Create a scenario and register it:
```java
public class MyScenario {
    public void onEnable() {
        FateAPI fateAPI = new FateAPI(UHC.getInstance());
        Scenario scenario = fateAPI.getScenarioManager().create(
                "My Scenario!", // Scenario Name
                Material.BEDROCK, // Display Item of the scenario
                false, // If you can toggle this scenario only in the lobby or anytime
                "Some very", // Description, this is a vararg. Every vararg is a newline.
                "cool description!"
        );
        fateAPI.getScenarioManager().add(scenario);
        // This registers your listener, check the example below on how that works.
        new MyScenarioListener(fateAPI.getScenarioManager(), scenario);
    }
}
```

Create a scenario listener that says `You moved!` when you move in-game:
```java
public class MyScenarioListener extends ScenarioListener {
    public MyScenarioListener(ScenarioManager scenarioManager, Scenario scenario) {
        super(scenarioManager, scenario);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        // This only runs when the scenario is active!
        event.getPlayer().sendMessage("You moved!");
    }
}
```