package pl.put.poznan.sqc.visitor;

import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

/**
 * Klasa odpowiedzialna za liczenie wszystkich kroków scenariusza,
 * również tych znajdujących się w podscenariuszach.
 */

public class StepCountVisitor implements ScenarioVisitor {

    private int stepCount = 0;

    /**
     * Przechodzi przez wszystkie kroki danego scenariusza.
     * @param scenario scenariusz do przeanalizowania
     */
    
    @Override
    public void visit(Scenario scenario) {
        for (Step step : scenario.getSteps()) {
            step.accept(this);
        }
    }

    /**
     * Zwiększa licznik kroków i sprawdza podscenariusze danego kroku.
     * @param step aktualnie analizowany krok
     */
    
    @Override
    public void visit(Step step) {
        stepCount++;

        for (Scenario subScenario : step.getSubScenarios()) {
            subScenario.accept(this);
        }
    }

    /**
     * Zwraca liczbę wszystkich policzonych kroków.
     * @return liczba kroków scenariusza
     */
    
    public int getStepCount() {
        return stepCount;
    }
}
