package de.dhbw.karlsruhe.derivation.leftRight.models;

import de.dhbw.karlsruhe.models.GrammarProduction;

import java.util.Objects;

public class SpecificDerivationValidationDetailResult {

    private boolean correct;
    private GrammarProduction wrongProduction;
    private String message = "";

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpecificDerivationValidationDetailResult that = (SpecificDerivationValidationDetailResult) o;
        if (wrongProduction == null && that.wrongProduction == null)
            return correct == that.correct && message.equals(that.message);
        return correct == that.correct && wrongProduction.equals(that.wrongProduction) && message.equals(that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correct, wrongProduction, message);
    }
}
