package pl.put.poznan.sqc.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testy jednostkowe weryfikujące mapowanie struktury scenariusza z i do formatu JSON.
 */
class ScenarioJsonTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldDeserializeScenarioWithNestedSubScenarios() throws Exception {
        String json = "{"
                + "\"title\": \"Dodanie książki\","
                + "\"actors\": [\"Bibliotekarz\"],"
                + "\"systemActor\": \"System\","
                + "\"steps\": ["
                + "  {\"text\": \"Bibliotekarz wybiera opcję dodania książki\"},"
                + "  {"
                + "    \"text\": \"IF: książka już istnieje\","
                + "    \"subScenarios\": ["
                + "      {"
                + "        \"title\": \"Obsługa duplikatu\","
                + "        \"actors\": [\"Bibliotekarz\"],"
                + "        \"systemActor\": \"System\","
                + "        \"steps\": ["
                + "          {\"text\": \"System wyświetla komunikat o błędzie\"}"
                + "        ]"
                + "      }"
                + "    ]"
                + "  }"
                + "]"
                + "}";

        Scenario scenario = objectMapper.readValue(json, Scenario.class);

        assertEquals("Dodanie książki", scenario.getTitle());
        assertEquals(1, scenario.getActors().size());
        assertEquals("System", scenario.getSystemActor());
        assertEquals(2, scenario.getSteps().size());

        Step firstStep = scenario.getSteps().get(0);
        assertNotNull(firstStep.getSubScenarios());
        assertTrue(firstStep.getSubScenarios().isEmpty());

        Step secondStep = scenario.getSteps().get(1);
        assertEquals(1, secondStep.getSubScenarios().size());
        assertEquals("Obsługa duplikatu", secondStep.getSubScenarios().get(0).getTitle());
        assertEquals(1, secondStep.getSubScenarios().get(0).getSteps().size());
    }

    @Test
    void shouldInitializeEmptyListsAndIgnoreUnknownFields() throws Exception {
        String minimalJson = "{\"title\": \"Pusty scenariusz\", \"extraField\": 123}";

        Scenario scenario = objectMapper.readValue(minimalJson, Scenario.class);

        assertEquals("Pusty scenariusz", scenario.getTitle());
        assertNotNull(scenario.getActors());
        assertTrue(scenario.getActors().isEmpty());
        assertNotNull(scenario.getSteps());
        assertTrue(scenario.getSteps().isEmpty());
    }
}