package de.dhbw.karlsruhe.controller.dto;

import de.dhbw.karlsruhe.bottom.up.models.BottomUpAcceptor;
import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownAcceptor;

public class ValidationBottomUpParamModel {

    private Grammar grammar;
    private BottomUpAcceptor bottomUpAcceptor;
    private String word;

    public ValidationBottomUpParamModel() {
    }

    public Grammar getGrammar() {
        return grammar;
    }

    public BottomUpAcceptor getBottomUpAcceptor() {
        return bottomUpAcceptor;
    }

    public String getWord() {
        return word;
    }
}
