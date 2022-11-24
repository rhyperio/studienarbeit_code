package de.dhbw.karlsruhe.top.down.parsing.models;

import de.dhbw.karlsruhe.models.GrammarRule;

public class TopDownStep {
    private String readInput;
    private TopDownState newState;
    private String newStack;
    private GrammarRule usedProduction;

    public TopDownStep(String pReadInput, TopDownState pNewState, String pNewStack, GrammarRule pUsedProduction) {
        this.setReadInput(pReadInput);
        this.setNewState(pNewState);
        this.setNewStack(pNewStack);
        this.setUsedProduction(pUsedProduction);
    }

    // TODO: implement toString

    public String getReadInput() {
        return readInput;
    }

    public void setReadInput(String readInput) {
        this.readInput = readInput;
    }

    public TopDownState getNewState() {
        return newState;
    }

    public void setNewState(TopDownState newState) {
        this.newState = newState;
    }

    public String getNewStack() {
        return newStack;
    }

    public void setNewStack(String newStack) {
        this.newStack = newStack;
    }

    public GrammarRule getUsedProduction() {
        return usedProduction;
    }

    public void setUsedProduction(GrammarRule usedProduction) {
        this.usedProduction = usedProduction;
    }
}
