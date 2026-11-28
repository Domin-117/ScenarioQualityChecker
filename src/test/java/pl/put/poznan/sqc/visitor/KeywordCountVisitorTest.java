
package pl.put.poznan.sqc.visitor;

import org.junit.jupiter.api.Test;
import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Klasa sprawdzająca poprawność liczenia kroków
 * rozpoczynających się od słów kluczowych.
 */

public class KeywordCountVisitorTest {

        /**
         * Sprawdza, czy scenariusz bez słów kluczowych daje wynik zero.
         */

    @Test
    public void noKeywordsReturnsZero() {

        Scenario scenario = new Scenario(
                "Scenariusz bez warunkow",
                Collections.emptyList(),
                "System",
                Arrays.asList(
                        new Step("System otwiera formularz", Collections.emptyList()),
                        new Step("Uzytkownik wpisuje dane", Collections.emptyList())
                )
        );

        KeywordCountVisitor visitor = new KeywordCountVisitor();

        scenario.accept(visitor);

        assertEquals(0, visitor.getKeywordCount());
    }

        /**
        * Sprawdza liczenie kroków rozpoczynających się od IF, ELSE i FOR EACH.
        */

    @Test
    public void countsKeywords() {

        Scenario scenario = new Scenario(
                "Scenariusz z warunkami",
                Collections.emptyList(),
                "System",
                Arrays.asList(
                        new Step("IF: Uzytkownik jest zalogowany", Collections.emptyList()),
                        new Step("ELSE: Pokaz komunikat", Collections.emptyList()),
                        new Step("FOR EACH: Produkt na liscie", Collections.emptyList()),
                        new Step("System wyswietla strone", Collections.emptyList())
                )
        );

        KeywordCountVisitor visitor = new KeywordCountVisitor();

        scenario.accept(visitor);

        assertEquals(3, visitor.getKeywordCount());
    }

        /**
         * Sprawdza liczenie słów kluczowych w scenariuszu i podscenariuszach.
         */

    @Test
    public void countsKeywordsInSubScenarios() {

        Scenario subScenario = new Scenario(
                "Podscenariusz",
                Collections.emptyList(),
                "System",
                Arrays.asList(
                        new Step("IF: Dane sa poprawne", Collections.emptyList()),
                        new Step("System zapisuje dane", Collections.emptyList())
                )
        );

        Scenario scenario = new Scenario(
                "Scenariusz glowny",
                Collections.emptyList(),
                "System",
                Arrays.asList(
                        new Step("ELSE: Pokaz blad",
                                Collections.singletonList(subScenario)),
                        new Step("System konczy dzialanie", Collections.emptyList())
                )
        );

        KeywordCountVisitor visitor = new KeywordCountVisitor();

        scenario.accept(visitor);

        assertEquals(2, visitor.getKeywordCount());
    }
}
