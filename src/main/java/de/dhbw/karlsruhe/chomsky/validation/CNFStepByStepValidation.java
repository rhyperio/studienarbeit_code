package de.dhbw.karlsruhe.chomsky.validation;

import de.dhbw.karlsruhe.models.Grammar;

public class CNFStepByStepValidation {
    public CNFStepByStepValidation() {
    }

    public boolean validateGrammarIsInCorrectCNF(Grammar finalGrammarToValidate, Grammar firstStepGrammar, Grammar secondStepGrammar, Grammar thirdStepGrammar) {
        if (!this.grammarIsValidAfterFirstStep(firstStepGrammar)) {
            return false;
        } else if (!this.grammarIsValidAfterSecondStep(secondStepGrammar)) {
            return false;
        } else if (!this.grammarIsValidAfterThirdStep(thirdStepGrammar)) {
            return false;
        } else if (!this.grammarIsValidAfterFinalStep(finalGrammarToValidate)) {
            return false;
        }

        return true;
    }

    private boolean grammarIsValidAfterFirstStep(Grammar firstStepGrammar) {
        return false;
    }

    private boolean grammarIsValidAfterSecondStep(Grammar secondStepGrammar) {
        return false;
    }

    private boolean grammarIsValidAfterThirdStep(Grammar thirdStepGrammar) {
        return false;
    }

    private boolean grammarIsValidAfterFinalStep(Grammar finalGrammarToValidate) {
        return false;
    }
}