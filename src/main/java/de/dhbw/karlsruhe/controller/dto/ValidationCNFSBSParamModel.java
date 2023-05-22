package de.dhbw.karlsruhe.controller.dto;

import de.dhbw.karlsruhe.models.Grammar;

public class ValidationCNFSBSParamModel {

    private Grammar grammar;
    private Grammar[] grammarTransformationSteps;
    public ValidationCNFSBSParamModel() {}

    public Grammar getGrammar() {
        return this.grammar;
    }

    public Grammar[] getGrammarStepsToValidate() {
        return this.grammarTransformationSteps;
    }
}
