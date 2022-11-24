package de.dhbw.karlsruhe.top.down.parsing.models;

import de.dhbw.karlsruhe.models.GrammarRule;

import java.util.ArrayList;
import java.util.List;

public class TopDownAcceptor {

    private List<TopDownStep> topDownSteps = new ArrayList<>();

    public void addStep(String pReadInput, TopDownState pNewState, String pNewStack, GrammarRule pUsedProduction) {
        TopDownStep tdstep = new TopDownStep(pReadInput, pNewState, pNewStack, pUsedProduction);

        this.topDownSteps.add(tdstep);
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
}
