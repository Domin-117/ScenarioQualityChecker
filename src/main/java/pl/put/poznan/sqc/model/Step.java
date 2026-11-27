package pl.put.poznan.sqc.model;

import pl.put.poznan.sqc.visitor.ScenarioVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje pojedynczy krok scenariusza oraz ewentualne zagnieżdżone pod-scenariusze.
 */
public class Step {
    private String text;
    private List<Scenario> subScenarios = new ArrayList<>();

    public Step() {
    }

    public Step(String text, List<Scenario> subScenarios) {
        this.text = text;
        setSubScenarios(subScenarios);
    }

    public void accept(ScenarioVisitor visitor) {
        visitor.visit(this);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Scenario> getSubScenarios() {
        return subScenarios;
    }

    public void setSubScenarios(List<Scenario> subScenarios) {
        this.subScenarios = (subScenarios != null) ? subScenarios : new ArrayList<>();
    }
}