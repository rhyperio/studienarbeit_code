package de.dhbw.karlsruhe.bottom.up.models;

import de.dhbw.karlsruhe.models.GrammarRule;
import de.dhbw.karlsruhe.models.ParserState;

import java.util.ArrayList;
import java.util.List;

public class BottomUpAcceptor {

    private List<BottomUpStep> steps = new ArrayList<>();

    public void addStep(BottomUpStep step){
        steps.add(step);
    }

    public void addStep(String stack, ParserState state, String remainingWord, GrammarRule production){
        BottomUpStep step = new BottomUpStep(stack, state, remainingWord, production);
        steps.add(step);
    }

    @Override
    public String toString() {
        return "BottomUpAcceptor{" +
                "steps=" + steps +
                '}';
    }

    public List<BottomUpStep> getSteps() {
        return steps;
    }
}
