package fr.wmidas.nightrush.scenarios;

import java.util.HashMap;
import java.util.Map;

public class ScenarioManager {

    private final Map<String, Scenario> scenarios = new HashMap<>();

    public ScenarioManager() {
        // Instancie tous les scénarios
        addScenario(new BloodMoon());
        addScenario(new FogOfWar());
        addScenario(new HunterBeast());
        addScenario(new TeamSwap());
        addScenario(new SabotageMode());
        addScenario(new SilentAttack());
        addScenario(new StormNight());
        addScenario(new Zizanie());
        addScenario(new WMidas());
    }

    private void addScenario(Scenario scenario) {
        scenarios.put(scenario.getName(), scenario);
    }

    public Scenario getScenario(String name) {
        return scenarios.get(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends Scenario> T getScenario(Class<T> clazz) {
        for (Scenario s : scenarios.values()) {
            if (clazz.isInstance(s)) return (T) s;
        }
        return null;
    }

    public void setScenarioActive(String name, boolean active) {
        Scenario scenario = scenarios.get(name);
        if (scenario != null) {
            if (scenario instanceof BloodMoon bm) bm.setActive(active);
            else if (scenario instanceof FogOfWar fow) fow.setActive(active);
            else if (scenario instanceof HunterBeast hb) hb.setActive(active);
            else if (scenario instanceof TeamSwap ts) ts.setActive(active);
            else if (scenario instanceof SabotageMode sm) sm.setActive(active);
            else if (scenario instanceof SilentAttack sa) sa.setActive(active);
            else if (scenario instanceof StormNight st) st.setActive(active);
            else if (scenario instanceof Zizanie zi) zi.setActive(active);
            else if (scenario instanceof WMidas wm) wm.setActive(active);
        }
    }

    public boolean isScenarioActive(String name) {
        Scenario scenario = scenarios.get(name);
        if (scenario == null) return false;
        if (scenario instanceof BloodMoon bm) return bm.active;
        if (scenario instanceof FogOfWar fow) return fow.active;
        if (scenario instanceof HunterBeast hb) return hb.active;
        if (scenario instanceof TeamSwap ts) return ts.active;
        if (scenario instanceof SabotageMode sm) return sm.active;
        if (scenario instanceof SilentAttack sa) return sa.active;
        if (scenario instanceof StormNight st) return st.active;
        if (scenario instanceof Zizanie zi) return zi.active;
        if (scenario instanceof WMidas wm) return wm.active;
        return false;
    }

    public void onNightStart() {
        scenarios.values().forEach(Scenario::onNightStart);
    }

    public void onNightEnd() {
        scenarios.values().forEach(Scenario::onNightEnd);
    }
}
