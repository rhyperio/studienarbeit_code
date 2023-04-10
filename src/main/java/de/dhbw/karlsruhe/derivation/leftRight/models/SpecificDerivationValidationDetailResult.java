package de.dhbw.karlsruhe.derivation.leftRight.models;

import de.dhbw.karlsruhe.models.GrammarProduction;

public class SpecificDerivationValidationDetailResult {

    private boolean correct;
    private GrammarProduction wrongProduction;
    private String message;

    public SpecificDerivationValidationDetailResult(boolean correct) {
        this.correct = correct;
    }

    public SpecificDerivationValidationDetailResult(boolean correct, String message) {
        this.correct = correct;
        this.message = message;
    }

    public SpecificDerivationValidationDetailResult(boolean correct, GrammarProduction wrongProduction, String message) {
        this.correct = correct;
        this.wrongProduction = wrongProduction;
        this.message = message;
    }

    public boolean isCorrect() {
        return correct;
    }

    public GrammarProduction getWrongProduction() {
        return wrongProduction;
    }

    public String getMessage() {
        return message;
    }
}
