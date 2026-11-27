package pl.put.poznan.sqc.logic;

import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

import java.util.ArrayList;
import java.util.List;
/**
 * Klasa odpowiedzialna za generowanie uproszczonej wersji scenariusza
 * poprzez ograniczenie głębokości zagnieżdzenia pod-scenariuszy.
 */
public class ScenarioSimplifier {

    /**
     * Tworzy głęboką kopię przekazanego scenariusza, zachowując pod-scenariusze
     * wyłącznie do określonego poziomu zagnieżdżenia.
     * @param scenario oryginalny scenariusz wejściowy
     * @param maxDepth maksymalny poziom zagnieżdzenia (1 oznacza główny scenariusz)
     * @return nowy obiekt {@link Scenario} przycięty do zadanego poziomu
     * @throws IllegalArgumentException gdy scenario jest null lub maxDepth jest mniejsze od 1
     */
    public Scenario simplify(Scenario scenario, int maxDepth) {
        if (scenario == null) throw new IllegalArgumentException("Scenariusz nie może być pusty (null)");
        if (maxDepth < 1) throw new IllegalArgumentException("Poziom zagnieżdzenia musi wynosić minimum 1");

        List<Step> simplifiedSteps = new ArrayList<>();
        for (Step step : scenario.getSteps()) {
            simplifiedSteps.add(copyStep(step, maxDepth));
        }

        return new Scenario(
                scenario.getTitle(),
                new ArrayList<>(scenario.getActors()),
                scenario.getSystemActor(),
                simplifiedSteps
        );
    }

    /**
     * Rekurencyjnie kopiuje pojedynczy krok oraz jego pod-scenariusze,
     * dopóki nie zostanie osiągnięty limit głębokości.
     * @param step orygialny krok do skopiowania
     * @param currDepth pozostała dopuszczalna głębokość zagnieżdżenia
     * @return skopiowany obiekt {@link Step}
     */
    private Step copyStep(Step step, int currDepth) {
        List<Scenario> simplifiedSubScenarios = new ArrayList<>();

        if (currDepth > 1) {
            for (Scenario subScenario : step.getSubScenarios()) {
                simplifiedSubScenarios.add(simplify(subScenario, currDepth - 1));
            }
        }

        return new Step(step.getText(), simplifiedSubScenarios);
    }

}
