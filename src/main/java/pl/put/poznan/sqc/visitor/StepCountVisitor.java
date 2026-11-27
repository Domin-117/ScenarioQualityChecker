package pl.put.poznan.sqc.visitor;

import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

public class StepCountVisitor implements ScenarioVisitor {

    private int stepCount = 0;

    @Override
    public void visit(Scenario scenario) {
        for (Step step : scenario.getSteps()) {
            step.accept(this);
        }
    }

    @Override
    public void visit(Step step) {
        stepCount++;

        for (Scenario subScenario : step.getSubScenarios()) {
            subScenario.accept(this);
        }
    }

    public int getStepCount() {
        return stepCount;
    }
}
