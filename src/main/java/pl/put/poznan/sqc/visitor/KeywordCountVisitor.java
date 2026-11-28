
package pl.put.poznan.sqc.visitor;

import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

/**
 * Klasa odpowiedzialna za liczenie kroków scenariusza
 * rozpoczynających się od IF, ELSE lub FOR EACH.
 */

public class KeywordCountVisitor implements ScenarioVisitor {

    private int keywordCount = 0;

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
     * Sprawdza, czy krok zaczyna się od słowa kluczowego,
     * zwiększa licznik i analizuje podscenariusze.
     * @param step aktualnie analizowany krok
     */

    @Override
    public void visit(Step step) {
        String text = step.getText();

        if (text != null) {
            text = text.trim();

            if (text.matches("^(IF|ELSE|FOR EACH)(:|\\s|$).*")) {
                keywordCount++;
            }
        }

        for (Scenario subScenario : step.getSubScenarios()) {
            subScenario.accept(this);
        }
    }

    /**
     * Zwraca liczbę kroków rozpoczynających się od słów kluczowych.
     * @return liczba znalezionych kroków
     */

    public int getKeywordCount() {
        return keywordCount;
    }
}
