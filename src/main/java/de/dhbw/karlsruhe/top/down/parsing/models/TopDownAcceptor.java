package de.dhbw.karlsruhe.top.down.parsing.models;

import de.dhbw.karlsruhe.models.GrammarRule;

import java.util.ArrayList;
import java.util.List;

public class TopDownAcceptor {

    private final List<TopDownStep> topDownSteps = new ArrayList<>();

    public void addStep(String readInput, TopDownState newState, String newStack, GrammarRule usedProduction) {
        TopDownStep tdStep = new TopDownStep(readInput, newState, newStack, usedProduction);

        this.topDownSteps.add(tdStep);
    }

    // TODO: implement toString

    public List<TopDownStep> getTopDownSteps() {
        return this.topDownSteps;
    }

    public TopDownStep getFirstStep() {
        return this.topDownSteps.get(0);
    }

    public TopDownStep getLastStep() {
        return this.topDownSteps.get(this.topDownSteps.size()-1);
    }
}
