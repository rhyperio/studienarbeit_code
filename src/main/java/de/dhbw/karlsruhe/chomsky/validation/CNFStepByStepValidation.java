package de.dhbw.karlsruhe.chomsky.validation;

import de.dhbw.karlsruhe.chomsky.generation.ChomskyTransformationGeneration;
import de.dhbw.karlsruhe.models.Grammar;

import java.util.*;

public class CNFStepByStepValidation {
    private Grammar[] grammarTransformationSteps;

    public CNFStepByStepValidation() {
    }

    public boolean validateGrammarIsInCorrectCNF(Grammar initialGrammar, Grammar[] grammarStepsToValidate) {
        ChomskyTransformationGeneration chomskyTransformationGeneration = new ChomskyTransformationGeneration();

        this.grammarTransformationSteps = chomskyTransformationGeneration.getStepByStepTransformedCNFGrammar(initialGrammar);

        if (!this.grammarEqualsAfterEpsilonResolution(grammarStepsToValidate[0])) {
            return false;
        } else if (!this.grammarEqualsAfterTerminalReplacement(grammarStepsToValidate[1])) {
            return false;
        } else if (!this.grammarEqualsAfterRightSideResolution(grammarStepsToValidate[2])) {
            return false;
        } else if (!this.grammarEqualsAfterSingleNonTerminalResolution(grammarStepsToValidate[3])) {
            return false;
        }

        return true;
    }

    private boolean grammarEqualsAfterEpsilonResolution(Grammar firstStepGrammar) {
        Grammar grammarAfterFirstStep = this.grammarTransformationSteps[0];

        if (Arrays.equals(grammarAfterFirstStep.getTerminals(), firstStepGrammar.getTerminals()) && Arrays.equals(grammarAfterFirstStep.getNonTerminals(), firstStepGrammar.getNonTerminals()) && grammarAfterFirstStep.getProductions().equals(firstStepGrammar.getProductions()) && grammarAfterFirstStep.getStartSymbol().equals(firstStepGrammar.getStartSymbol())) {
            return true;
        }

        return false;
    }

    private boolean grammarEqualsAfterTerminalReplacement(Grammar secondStepGrammar) {
        Grammar grammarAfterSecondStep = this.grammarTransformationSteps[1];

        if (Arrays.equals(grammarAfterSecondStep.getTerminals(), secondStepGrammar.getTerminals()) && Arrays.equals(grammarAfterSecondStep.getNonTerminals(), secondStepGrammar.getNonTerminals()) && grammarAfterSecondStep.getProductions().equals(secondStepGrammar.getProductions()) && grammarAfterSecondStep.getStartSymbol().equals(secondStepGrammar.getStartSymbol())) {
            return true;
        }

        return false;
    }

    private boolean grammarEqualsAfterRightSideResolution(Grammar thirdStepGrammar) {
        Grammar grammarAfterThirdStep = this.grammarTransformationSteps[2];

        if (Arrays.equals(grammarAfterThirdStep.getTerminals(), thirdStepGrammar.getTerminals()) && Arrays.equals(grammarAfterThirdStep.getNonTerminals(), thirdStepGrammar.getNonTerminals()) && grammarAfterThirdStep.getProductions().equals(thirdStepGrammar.getProductions()) && grammarAfterThirdStep.getStartSymbol().equals(thirdStepGrammar.getStartSymbol())) {
            return true;
        }

        return false;
    }

    private boolean grammarEqualsAfterSingleNonTerminalResolution(Grammar finalGrammarToValidate) {
        Grammar grammarAfterFinalStep = this.grammarTransformationSteps[3];

        if (Arrays.equals(grammarAfterFinalStep.getTerminals(), finalGrammarToValidate.getTerminals()) && Arrays.equals(grammarAfterFinalStep.getNonTerminals(), finalGrammarToValidate.getNonTerminals()) && grammarAfterFinalStep.getProductions().equals(finalGrammarToValidate.getProductions()) && grammarAfterFinalStep.getStartSymbol().equals(finalGrammarToValidate.getStartSymbol())) {
            return true;
        }

        return false;
    }

}