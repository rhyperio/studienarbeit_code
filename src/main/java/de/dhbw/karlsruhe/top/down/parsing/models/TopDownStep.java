package de.dhbw.karlsruhe.top.down.parsing.models;

import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.models.ParserState;

import java.util.Objects;

public class TopDownStep {
    private String readInput;
    private ParserState state;
    private String stack;
    private GrammarProduction usedProduction;

    public TopDownStep() {
    }

    public TopDownStep(String readInput, ParserState state, String stack, GrammarProduction usedProduction) {
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

    public GrammarProduction getUsedProduction() {
        return usedProduction;
    }

    public void setUsedProduction(GrammarProduction usedProduction) {
        this.usedProduction = usedProduction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TopDownStep that = (TopDownStep) o;
        return Objects.equals(readInput, that.readInput) && state == that.state && Objects.equals(stack, that.stack) && Objects.equals(usedProduction, that.usedProduction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(readInput, state, stack, usedProduction);
    }
}
