package de.dhbw.karlsruhe.controller.dto;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownAcceptor;

public class ValidationTopDownParamModel {
    private Grammar grammar;
    private TopDownAcceptor topDownAcceptor;
    private String word;

    public ValidationTopDownParamModel() {
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public TopDownAcceptor getTopDownAcceptor() {
        return topDownAcceptor;
    }

    public String getWord() {
        return word;
    }
}
