package pl.put.poznan.sqc.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.sqc.model.Scenario;
import pl.put.poznan.sqc.model.Step;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Główny kontroler REST API aplikacji Scenario Quality Checker.
 * Udostępnia endpointy do walidacji i analizy jakości scenariuszy w formacie JSON.
 */
@RestController
@RequestMapping("/api/scenarios")
public class ScenarioController {

    /**
     * Weryfikuje poprawność struktury przesłanego scenariusza i zwraca podstawowe metadane.
     *
     * @param scenario obiekt scenariusza zdeserializowany z żądania JSON
     * @return odpowiedź HTTP 200 z podsumowaniem scenariusza lub HTTP 400 przy błędnych danych
     */
    @PostMapping("/validate")
    public ResponseEntity<Object> validateScenario(@RequestBody(required = false) Scenario scenario) {
        ResponseEntity<Object> validationError = validateInput(scenario);
        if (validationError != null) {
            return validationError;
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("title", scenario.getTitle());
        response.put("actorsCount", scenario.getActors() != null ? scenario.getActors().size() : 0);
        response.put("systemActor", scenario.getSystemActor());
        response.put("topLevelStepsCount", scenario.getSteps().size());
        response.put("status", "OK");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Zwraca całkowitą liczbę kroków w scenariuszu z uwzględnieniem pod-scenariuszy.
     *
     * @param scenario obiekt scenariusza zdeserializowany z żądania JSON
     * @return odpowiedź HTTP 200 z liczbą kroków lub HTTP 400 przy błędnych danych
     */
    @PostMapping("/steps/count")
    public ResponseEntity<Object> countSteps(@RequestBody(required = false) Scenario scenario) {
        ResponseEntity<Object> validationError = validateInput(scenario);
        if (validationError != null) {
            return validationError;
        }

        // Po zmergowaniu StepCountVisitor przez Osobę 2 zastąp poniższe wywołanie wizytatorem
        int totalSteps = countStepsFallback(scenario);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("title", scenario.getTitle());
        response.put("stepCount", totalSteps);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Zwraca liczbę kroków rozpoczynających się od słów kluczowych (np. IF, ELSE, FOR EACH).
     *
     * @param scenario obiekt scenariusza zdeserializowany z żądania JSON
     * @return odpowiedź HTTP 200 z liczbą kroków warunkowych lub HTTP 400 przy błędnych danych
     */
    @PostMapping("/steps/keywords")
    public ResponseEntity<Object> countKeywordSteps(@RequestBody(required = false) Scenario scenario) {
        ResponseEntity<Object> validationError = validateInput(scenario);
        if (validationError != null) {
            return validationError;
        }

        // Po zmergowaniu KeywordCountVisitor przez Osobę 2 zastąp poniższe wywołanie wizytatorem
        int keywordSteps = 0;

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("title", scenario.getTitle());
        response.put("keywordStepCount", keywordSteps);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Obsługuje błędy składniowe przesłanego dokumentu JSON.
     *
     * @param ex wyjątek zgłaszany przez parser Jackson przy niepoprawnym JSON-ie
     * @return odpowiedź HTTP 400 Bad Request z komunikatem błędu w formacie JSON
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleInvalidJson(HttpMessageNotReadableException ex) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("error", "Nieprawidłowy format danych JSON");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> validateInput(Scenario scenario) {
        if (scenario == null) {
            return buildBadRequest("Brak obiektu scenariusza w treści żądania");
        }
        if (scenario.getTitle() == null || scenario.getTitle().trim().isEmpty()) {
            return buildBadRequest("Pole 'title' scenariusza nie może być puste");
        }
        if (scenario.getSteps() == null || scenario.getSteps().isEmpty()) {
            return buildBadRequest("Scenariusz musi zawierać co najmniej jeden krok w polu 'steps'");
        }
        return null;
    }

    private ResponseEntity<Object> buildBadRequest(String message) {
        Map<String, Object> error = new LinkedHashMap<>();
        error.put("error", message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private int countStepsFallback(Scenario scenario) {
        if (scenario == null || scenario.getSteps() == null) {
            return 0;
        }
        int count = 0;
        for (Step step : scenario.getSteps()) {
            count++;
            if (step.getSubScenarios() != null) {
                for (Scenario subScenario : step.getSubScenarios()) {
                    count += countStepsFallback(subScenario);
                }
            }
        }
        return count;
    }
}