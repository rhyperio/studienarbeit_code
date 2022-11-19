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

    public void setStack(String stack) {
        this.stack = stack;
    }

    public BottomUpState getState() {
        return state;
    }

    public void setState(BottomUpState state) {
        this.state = state;
    }

    public String getRemainingWord() {
        return remainingWord;
    }

    public void setRemainingWord(String remainingWord) {
        this.remainingWord = remainingWord;
    }

    public GrammarRule getProduction() {
        return production;
    }

    public void setProduction(GrammarRule production) {
        this.production = production;
    }
}
