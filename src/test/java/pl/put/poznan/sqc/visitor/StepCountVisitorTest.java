package pl.put.poznan.sqc.visitor;

import org.junit.jupiter.api.Test;
import pl.put.poznan.sqc.model.Scenario;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

import pl.put.poznan.sqc.model.Step;
import java.util.Arrays;

public class StepCountVisitorTest {

    @Test
    public void emptyScenarioHasZeroSteps() {

        Scenario scenario = new Scenario(
                "Pusty scenariusz",
                Collections.emptyList(),
                "System",
                Collections.emptyList()
        );

        StepCountVisitor visitor = new StepCountVisitor();

        scenario.accept(visitor);

        assertEquals(0, visitor.getStepCount());
    }


@Test
public void nestedScenarioCountsAllSteps() {

    Scenario subScenario = new Scenario(
            "Pod-scenariusz",
            Collections.emptyList(),
            "System",
            Arrays.asList(
                    new Step("Krok 1 pod-scenariusza",
                            Collections.emptyList()),
                    new Step("Krok 2 pod-scenariusza",
                            Collections.emptyList())
            )
    );

    Scenario scenario = new Scenario(
            "Scenariusz główny",
            Collections.emptyList(),
            "System",
            Arrays.asList(
                    new Step("Krok 1", Collections.emptyList()),
                    new Step("Krok 2",
                            Collections.singletonList(subScenario)),
                    new Step("Krok 3", Collections.emptyList())
            )
    );

    StepCountVisitor visitor = new StepCountVisitor();

    scenario.accept(visitor);

    assertEquals(5, visitor.getStepCount());
}


@Test
public void deeplyNestedScenarioCountsAllSteps() {

    Scenario deepest = new Scenario(
            "Poziom 3",
            Collections.emptyList(),
            "System",
            Collections.singletonList(
                    new Step("Krok 3.1", Collections.emptyList())
            )
    );

    Scenario middle = new Scenario(
            "Poziom 2",
            Collections.emptyList(),
            "System",
            Collections.singletonList(
                    new Step("Krok 2.1",
                            Collections.singletonList(deepest))
            )
    );

    Scenario main = new Scenario(
            "Poziom 1",
            Collections.emptyList(),
            "System",
            Arrays.asList(
                    new Step("Krok 1.1", Collections.emptyList()),
                    new Step("Krok 1.2",
                            Collections.singletonList(middle))
            )
    );

    StepCountVisitor visitor = new StepCountVisitor();

    main.accept(visitor);

    assertEquals(4, visitor.getStepCount());
}


}
