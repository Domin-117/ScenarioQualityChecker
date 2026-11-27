package pl.put.poznan.sqc.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testy integracyjne warstwy REST dla klasy ScenarioController.
 */
@WebMvcTest(ScenarioController.class)
class ScenarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCountStepsIncludingSubScenarios() throws Exception {
        String validJson = "{"
                + "\"title\": \"Logowanie\","
                + "\"actors\": [\"Użytkownik\"],"
                + "\"systemActor\": \"System\","
                + "\"steps\": ["
                + "  {\"text\": \"Użytkownik podaje login i hasło\"},"
                + "  {"
                + "    \"text\": \"IF: dane są niepoprawne\","
                + "    \"subScenarios\": ["
                + "      {"
                + "        \"title\": \"Błąd logowania\","
                + "        \"steps\": [{\"text\": \"System wyświetla komunikat błędu\"}]"
                + "      }"
                + "    ]"
                + "  }"
                + "]"
                + "}";

        mockMvc.perform(post("/api/scenarios/steps/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Logowanie"))
                .andExpect(jsonPath("$.stepCount").value(3));
    }

    @Test
    void shouldValidateScenarioAndReturnMetadata() throws Exception {
        String validJson = "{"
                + "\"title\": \"Rejestracja\","
                + "\"actors\": [\"Gość\"],"
                + "\"systemActor\": \"System\","
                + "\"steps\": [{\"text\": \"Gość wypełnia formularz\"}]"
                + "}";

        mockMvc.perform(post("/api/scenarios/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.actorsCount").value(1))
                .andExpect(jsonPath("$.topLevelStepsCount").value(1));
    }

    @Test
    void shouldReturnBadRequestWhenStepsAreMissing() throws Exception {
        String invalidJson = "{\"title\": \"Niekompletny scenariusz\"}";

        mockMvc.perform(post("/api/scenarios/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void shouldReturnBadRequestForMalformedJson() throws Exception {
        mockMvc.perform(post("/api/scenarios/steps/count")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{broken-json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void shouldReturnBadRequestWhenStepTextIsEmpty() throws Exception {
        String invalidJson = "{\"title\": \"Test\", \"steps\": [{\"text\": \"   \"}]}";

        mockMvc.perform(post("/api/scenarios/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
}