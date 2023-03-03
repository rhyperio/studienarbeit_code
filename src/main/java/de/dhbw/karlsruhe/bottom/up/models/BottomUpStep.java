package de.dhbw.karlsruhe.bottom.up.models;

import de.dhbw.karlsruhe.models.GrammarRule;
import de.dhbw.karlsruhe.models.ParserState;

public class BottomUpStep {

    private String stack;
    private ParserState state;
    private String remainingWord;
    private GrammarRule production;

    public BottomUpStep(String stack, ParserState state, String remainingWord, GrammarRule production) {
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

    public GrammarRule getProduction() {
        return production;
    }

}
