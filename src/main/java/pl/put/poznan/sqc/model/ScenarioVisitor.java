package pl.put.poznan.sqc.model;

/**
 * Interfejs wzorca Wizytator do analizy struktury scenariusza i jego kroków.
 */
public interface ScenarioVisitor {
    void visit(Scenario scenario);
    void visit(Step step);
}