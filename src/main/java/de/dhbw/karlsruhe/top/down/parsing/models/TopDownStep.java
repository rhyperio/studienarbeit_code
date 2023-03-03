package de.dhbw.karlsruhe.top.down.parsing.models;

import de.dhbw.karlsruhe.models.GrammarRule;
import de.dhbw.karlsruhe.models.ParserState;

public class TopDownStep {
    private String readInput;
    private ParserState state;
    private String stack;
    private GrammarRule usedProduction;

    public TopDownStep(String readInput, ParserState state, String stack, GrammarRule usedProduction) {
        this.setReadInput(readInput);
        this.setState(state);
        this.setStack(stack);
        this.setUsedProduction(usedProduction);
    }

    @Override
    public String toString() {
        return "TopDownStep{" +
                "readInput='" + readInput + '\'' +
                ", State=" + state +
                ", Stack='" + stack + '\'' +
                ", usedProduction=" + usedProduction +
                '}';
    }

    public String getReadInput() {
        return readInput;
    }

    public void setReadInput(String readInput) {
        this.readInput = readInput;
    }

    public ParserState getState() {
        return state;
    }

    public void setState(ParserState state) {
        this.state = state;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public GrammarRule getUsedProduction() {
        return usedProduction;
    }

    public void setUsedProduction(GrammarRule usedProduction) {
        this.usedProduction = usedProduction;
    }
}
