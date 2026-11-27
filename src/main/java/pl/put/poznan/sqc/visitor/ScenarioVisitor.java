package pl.put.poznan.sqc.visitor;

import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

/**
 * Interfejs wzorca Wizytator do analizy struktury scenariusza i jego kroków.
 */
public interface ScenarioVisitor {
    void visit(Scenario scenario);
    void visit(Step step);
}