package xyz.sculas.fateapi.scenario;

import lombok.NonNull;
import net.fateuhc.plugin.UHC;
import net.fateuhc.plugin.managers.game.ScenariosManager;
import net.fateuhc.plugin.scenarios.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.Nullable;
import sun.reflect.ConstructorAccessor;
import xyz.sculas.fateapi.scenario.listener.ScenarioListener;
import xyz.sculas.fateapi.scenario.listener.ScenarioToggleListener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class ScenarioManager {

    /**
     * Instance used privately by internal classes to utilize functions from FateUHC.
     */
    private final UHC fateInstance;
    /**
     * This list contains all listeners put in by the end-user.
     */
    private HashMap<Scenario, ScenarioListener> listeners;

    public ScenarioManager(@NonNull UHC fateInstance) {
        this.fateInstance = fateInstance;
        listeners = new HashMap<>();
        new ScenarioToggleListener(this);
    }

    /**
     * Add a scenario made by using {@link ScenarioManager#create(String, Material, boolean, String...)}
     * @param scenario the scenario you made
     * @return the scenario used in the argument, could be used for chaining
     * @see ScenarioManager#create(String, Material, boolean, String...)
     */
    public Scenario add(@NonNull Scenario scenario) {
        List<Scenario> l = raw().getScenarios();
        if(l.contains(scenario)) throw new IllegalStateException("Scenario already exists (preventing duplicates)");
        l.add(scenario);
        raw().setScenarios(l);
        return scenario;
    }

    /**
     * Remove a scenario you added
     * @param scenario the scenario you added that needs to be removed
     * @return the scenario used in the argument, could be used for chaining
     * @see ScenarioManager#add(Scenario)
     */
    public Scenario remove(@NonNull Scenario scenario) {
        List<Scenario> l = raw().getScenarios();
        if(!l.contains(scenario)) throw new IllegalStateException("Scenario has not been added yet");
        l.remove(scenario);
        raw().setScenarios(l);
        unregister(scenario);
        return scenario;
    }

    /**
     * Create a scenario
     * @param name name of the scenario
     * @param displayItem item the scenario should display in <code>/config admin</code>
     * @param lobbyModeOnly if this scenario can only be toggled while not in a game
     * @param description the description of the scenario, <b>vararg</b>
     * @return the created scenario
     */
    @Nullable
    public Scenario create(@NonNull String name, @NonNull Material displayItem, boolean lobbyModeOnly, String... description) {
        try {
            // This was not fun to implement, for real. Reflection and enums are not fun to work with :)
            Constructor<Scenario> c = Scenario.class.getDeclaredConstructor(String.class, int.class, Material.class, String.class, boolean.class, String[].class);
            c.setAccessible(true);
            Field caf = Constructor.class.getDeclaredField("constructorAccessor");
            caf.setAccessible(true);
            ConstructorAccessor ca = (ConstructorAccessor) caf.get(c);
            if (ca == null) {
                Method acquireConstructorAccessorMethod = Constructor.class.getDeclaredMethod("acquireConstructorAccessor");
                acquireConstructorAccessorMethod.setAccessible(true);
                ca = (ConstructorAccessor) acquireConstructorAccessorMethod.invoke(c);
            }
            return (Scenario) ca.newInstance(new Object[]{ name.trim().toUpperCase(), raw().getScenarios().size()+1, displayItem, name, lobbyModeOnly, description });
            //TODO: this is for Noodles (dev of FateUHC) to impl for himself
            //      return new Scenario(displayItem, name, lobbyModeOnly, description);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Was not able to create a new scenario using Reflection in FateUHC! See error above.");
        }
    }

    /**
     * Registers the scenario listener.<br>
     * <b>This is only used internally and should not be used externally.</b>
     * @param scenario the scenario to map this listener to
     * @param scenarioListener the listener to register
     */
    public void register(@NonNull Scenario scenario, @NonNull ScenarioListener scenarioListener) {
        if(listeners.containsValue(scenarioListener)) throw new IllegalStateException("Scenario already registered (preventing duplicates)");
        listeners.put(scenario, scenarioListener);
        if(scenario.isActive()) Bukkit.getPluginManager().registerEvents(scenarioListener, UHC.getInstance());
    }

    /**
     * Unregisters the scenario listener the scenario in this argument has been mapped to.<br>
     * <b>This is only used internally and should not be used externally.</b>
     * @param scenario the scenario a listener has been mapped to
     */
    public void unregister(@NonNull Scenario scenario) {
        listeners.remove(scenario);
        HandlerList.unregisterAll(getListener(scenario));
        scenario.setActive(false);
    }

    /**
     * Gets the listener you've registered before
     * @param scenario the scenario that a listener has been mapped to
     * @return the scenario listener
     * @see ScenarioListener
     */
    @Nullable
    public ScenarioListener getListener(@NonNull Scenario scenario) {
        return listeners.get(scenario);
    }

    /**
     * Returns the raw scenario manager from the actual UHC implementation.<br>
     * <b>Keep in mind this is obfuscated, so arguments are harder to read.</b>
     * @return the raw scenario manager from the actual UHC implementation.
     */
    public ScenariosManager raw() {
        return fateInstance.getScenariosManager();
    }

    /**
     * Disposes all listeners.
     */
    public void dispose() {
        listeners.forEach((__, l) -> HandlerList.unregisterAll(l));
        listeners.clear();
    }

}
