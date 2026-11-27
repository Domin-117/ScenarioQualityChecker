package pl.put.poznan.sqc.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.put.poznan.sqc.logic.ScenarioSimplifier;
import pl.put.poznan.sqc.model.Scenario;


/**
 * Kontroler REST udostępniający funkcjonalność upraszczania scenariuszy
 * do podanego poziomu zagnieżdżenia pod-scenariuszy.
 */
@RestController
@RequestMapping("/api/scenarios")
public class ScenarioSimplifierController {

    private static final Logger logger = LoggerFactory.getLogger(ScenarioSimplifierController.class);
    private final ScenarioSimplifier scenarioSimplifier;

    public ScenarioSimplifierController() {
        this.scenarioSimplifier = new ScenarioSimplifier();
    }

    /**
     * Zwraca uproszczoną wersję przesłanego scenariusza ograniczoną do podanej głębokości
     * Wartość poziomu 1 oznacza zwrócenie kroków głównego scenariusza.
     * @param scenario obiekt scenariusza przesłany w formacie JSON w treści żądania
     * @param level maksymalny poziom zagnieżdżenia
     * @return odpowiedź HTTP 200 z uproszczonym scenariuszem lub HTTP 400 w przypadku błędu
     */
    @PostMapping("/simplify")
    public ResponseEntity<?> simplifyScenario(
            @RequestBody Scenario scenario,
            @RequestParam(name = "level", defaultValue = "1") int level) {
        logger.info("Otrzymano żądanie uproszczenia scenariusza od poziomu: {}", level);
        logger.debug("Szczegóły wejściowego scenariusza: {}", scenario);

        if (level < 1) {
            logger.warn("Odrzucono żądanie z powodu nieprawdiłowego poziomu głębokości: {}", level);
            return ResponseEntity.badRequest().body("Treść żądania musi zawierać poprawny obiekt scenariusza");
        }

        try {
            Scenario simplified = scenarioSimplifier.simplify(scenario, level);
            logger.debug("Zwracany uproszczony scenariusz: {}", simplified);
            return ResponseEntity.ok(simplified);
        } catch (IllegalArgumentException e) {
            logger.error("Błąd walidacji podczas upraszczania scenariusza: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}