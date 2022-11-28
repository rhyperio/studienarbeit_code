package de.dhbw.karlsruhe.top.down.parsing.models;

import de.dhbw.karlsruhe.models.GrammarRule;

public class TopDownStep {
    private String readInput;
    private TopDownState newState;
    private String newStack;
    private GrammarRule usedProduction;

    public TopDownStep(String readInput, TopDownState newState, String newStack, GrammarRule usedProduction) {
        this.setReadInput(readInput);
        this.setNewState(newState);
        this.setNewStack(newStack);
        this.setUsedProduction(usedProduction);
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
