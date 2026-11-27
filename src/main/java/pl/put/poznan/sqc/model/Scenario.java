package pl.put.poznan.sqc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import pl.put.poznan.sqc.visitor.ScenarioVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Reprezentuje scenariusz lub pod-scenariusz wraz z nagłówkiem i listą kroków.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Scenario {
    private String title;
    private List<String> actors = new ArrayList<>();
    private String systemActor;
    private List<Step> steps = new ArrayList<>();

    public Scenario() {
    }

    public Scenario(String title, List<String> actors, String systemActor, List<Step> steps) {
        this.title = title;
        setActors(actors);
        this.systemActor = systemActor;
        setSteps(steps);
    }

    public void accept(ScenarioVisitor visitor) {
        visitor.visit(this);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = (actors != null) ? actors : new ArrayList<>();
    }

    public String getSystemActor() {
        return systemActor;
    }

    public void setSystemActor(String systemActor) {
        this.systemActor = systemActor;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = (steps != null) ? steps : new ArrayList<>();
    }
}