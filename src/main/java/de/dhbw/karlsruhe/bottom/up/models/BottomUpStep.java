package de.dhbw.karlsruhe.bottom.up.models;

import de.dhbw.karlsruhe.models.GrammarRule;

public class BottomUpStep {

    private String stack;
    private BottomUpState state;
    private String remainingWord;
    private GrammarRule production;

    public BottomUpStep(String stack, BottomUpState state, String remainingWord, GrammarRule production) {
        this.stack = stack;
        this.state = state;
        this.remainingWord = remainingWord;
        this.production = production;
    }

    public String getStack() {
        return stack;
    }

    public BottomUpState getState() {
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
