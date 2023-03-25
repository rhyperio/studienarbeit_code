package de.dhbw.karlsruhe.bottom.up.models;

import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.models.ParserState;

import java.util.Objects;

public class BottomUpStep {

    private String stack;
    private ParserState state;
    private String remainingWord;
    private GrammarProduction production;

    public BottomUpStep(String stack, ParserState state, String remainingWord, GrammarProduction production) {
        this.stack = stack;
        this.state = state;
        this.remainingWord = remainingWord;
        this.production = production;
    }

    public String getStack() {
        return stack;
    }

    public ParserState getState() {
        return state;
    }

    public String getRemainingWord() {
        return remainingWord;
    }

    @Override
    public String toString() {
        return "BottomUpStep{" +
                "stack='" + stack + '\'' +
                ", state=" + state +
                ", remainingWord='" + remainingWord + '\'' +
                ", production=" + production +
                '}';
    }

    public GrammarProduction getProduction() {
        return production;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BottomUpStep that = (BottomUpStep) o;
        return stack.equals(that.stack) && state == that.state && remainingWord.equals(that.remainingWord) && Objects.equals(production, that.production);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stack, state, remainingWord, production);
    }
}
