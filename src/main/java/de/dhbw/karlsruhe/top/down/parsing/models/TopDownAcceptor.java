package de.dhbw.karlsruhe.top.down.parsing.models;

import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.models.ParserState;

import java.util.ArrayList;
import java.util.List;

public class TopDownAcceptor {

    private final List<TopDownStep> topDownSteps = new ArrayList<>();

    public void addStep(String readInput, ParserState newState, String newStack, GrammarProduction usedProduction) {
        TopDownStep tdStep = new TopDownStep(readInput, newState, newStack, usedProduction);

        this.topDownSteps.add(tdStep);
    }

    @Override
    public String toString() {
        return "TopDownAcceptor{" +
                "topDownSteps=" + topDownSteps +
                '}';
    }

    public List<TopDownStep> getTopDownSteps() {
        return this.topDownSteps;
    }

    public TopDownStep getFirstStep() {
        return this.topDownSteps.get(0);
    }

    public TopDownStep getLastStep() {
        return this.topDownSteps.get(this.topDownSteps.size()-1);
    }

    public TopDownStep getSecondLastStep() {
        return this.topDownSteps.get(this.topDownSteps.size()-2);
    }
}
