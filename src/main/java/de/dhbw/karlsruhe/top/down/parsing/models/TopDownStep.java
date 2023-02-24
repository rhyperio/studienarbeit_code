package de.dhbw.karlsruhe.top.down.parsing.models;

import de.dhbw.karlsruhe.models.GrammarRule;
import de.dhbw.karlsruhe.models.ParserState;

public class TopDownStep {
    private String readInput;
    private ParserState newState;
    private String newStack;
    private GrammarRule usedProduction;

    public TopDownStep(String readInput, ParserState newState, String newStack, GrammarRule usedProduction) {
        this.setReadInput(readInput);
        this.setNewState(newState);
        this.setNewStack(newStack);
        this.setUsedProduction(usedProduction);
    }

    @Override
    public String toString() {
        return "TopDownStep{" +
                "readInput='" + readInput + '\'' +
                ", newState=" + newState +
                ", newStack='" + newStack + '\'' +
                ", usedProduction=" + usedProduction +
                '}';
    }

    public String getReadInput() {
        return readInput;
    }

    public void setReadInput(String readInput) {
        this.readInput = readInput;
    }

    public ParserState getNewState() {
        return newState;
    }

    public void setNewState(ParserState newState) {
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
