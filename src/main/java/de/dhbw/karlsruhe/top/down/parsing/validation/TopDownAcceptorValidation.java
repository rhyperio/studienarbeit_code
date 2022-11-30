package de.dhbw.karlsruhe.top.down.parsing.validation;

import de.dhbw.karlsruhe.models.GrammarRule;
import de.dhbw.karlsruhe.services.GrammarService;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownAcceptor;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownState;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownStep;

import java.util.Arrays;
import java.util.List;

public class TopDownAcceptorValidation {

    private GrammarService grammarService;
    private List<GrammarRule> grammarRules;
    private TopDownAcceptor tdAcceptor;
    private String wordToPars;

    public TopDownAcceptorValidation(String grammarAsJson) {
        this.grammarService = new GrammarService(grammarAsJson);
        this.grammarRules = this.grammarService.getGrammarRules();
    }

    public boolean validateTopDownAcceptor(TopDownAcceptor pTDAcceptor, String pWord) {
        this.tdAcceptor = pTDAcceptor;
        this.wordToPars = pWord;

        if (!this.validateFirstStep() || !this.validateLastStep() || !this.validateSteps()) {
            return false;
        }
        return true;
    }

    private boolean validateFirstStep() {
        boolean success = false;

        TopDownStep stepToCheck = this.tdAcceptor.getFirstStep();
        TopDownState currentState = stepToCheck.getNewState();
        String stack = stepToCheck.getNewStack();
        String input = stepToCheck.getReadInput();
        GrammarRule usedProduction = stepToCheck.getUsedProduction();

        if (currentState == TopDownState.Z0 && stack.equals("*") && input.equals("") && usedProduction == null) {
            success = true;
        }

        return success;
    }

    private boolean validateLastStep() {
        boolean success = false;

        TopDownStep stepToCheck = this.tdAcceptor.getLastStep();
        TopDownState currentState = stepToCheck.getNewState();
        String stack = stepToCheck.getNewStack();
        String input = stepToCheck.getReadInput();
        GrammarRule usedProduction = stepToCheck.getUsedProduction();

        if (currentState == TopDownState.ZF && stack.equals("*") && input.equals(this.wordToPars) && usedProduction == null) {
            success = true;
        }

        return success;
    }

    private boolean validateSteps() {
        List<TopDownStep> tdSteps = this.tdAcceptor.getTopDownSteps();
        TopDownStep nextStep = null;
        TopDownStep currStep = null;
        boolean success = false;

        for (int i=0; i < tdSteps.size(); i++) {
            currStep = tdSteps.get(i);

            if (currStep.equals(this.tdAcceptor.getFirstStep()) || currStep.equals(this.tdAcceptor.getLastStep())) {
                continue;
            }

            nextStep = tdSteps.get(i+1);
            String firstStackChar = String.valueOf(currStep.getNewStack().charAt(0));

            if (Arrays.stream(this.grammarService.getTerminals()).anyMatch(firstStackChar::equals)) {
                success = this.validateReadStep(currStep, nextStep);
            } else if (Arrays.stream(this.grammarService.getNonTerminals()).anyMatch(firstStackChar::equals)) {
                success = this.validateProductionStep(currStep, nextStep);
            }
        }

        return success;
    }

    private boolean validateReadStep(TopDownStep tdStep, TopDownStep nextStep) {
        boolean valid = false;

        String stackNextStep = nextStep.getNewStack();
        String stackCurrStep = tdStep.getNewStack();
        String inputNextStep = nextStep.getReadInput();
        GrammarRule productionCurrStep = tdStep.getUsedProduction();
        TopDownState stateCurrStep = tdStep.getNewState();

        if (stackCurrStep == null || stateCurrStep != TopDownState.Z) {
            return false;
        }

        if (this.checkReadInput(stackCurrStep, inputNextStep) && this.checkSideConditionsRead(stackCurrStep, stackNextStep, productionCurrStep)) {
            valid = true;
        }

        return valid;
    }

    private boolean checkReadInput(String stackCurrStep, String inputNextStep) {
        boolean success = false;

        char firstCharStackCurrStep = stackCurrStep.charAt(0);
        char firstCharInputNextStep = inputNextStep.charAt(inputNextStep.length()-1);

        if (firstCharStackCurrStep == firstCharInputNextStep) {
            success = true;
        }

        return success;
    }

    private boolean checkSideConditionsRead(String stackCurrStep, String stackNextStep, GrammarRule productionCurrStep) {
        boolean success = false;

        char secondCharStackCurrStep = stackCurrStep.charAt(1);
        char firstCharStackNextStep = stackNextStep.charAt(0);

        if (productionCurrStep == null && (secondCharStackCurrStep == firstCharStackNextStep)) {
            success = true;
        }

        return success;
    }

    private boolean validateProductionStep(TopDownStep tdStep, TopDownStep nextStep) {
        boolean valid = false;

        String stackCurrStep = tdStep.getNewStack();
        String stackNextStep = nextStep.getNewStack();
        GrammarRule productionCurrStep = tdStep.getUsedProduction();
        String inputCurrStep = tdStep.getReadInput();
        String inputNextStep = nextStep.getReadInput();
        TopDownState stateCurrStep = tdStep.getNewState();

        if (stackCurrStep == null || productionCurrStep == null || stateCurrStep != TopDownState.Z) {
            return false;
        }

        if (this.checkUsedProduction(stackCurrStep, stackNextStep, productionCurrStep) && this.checkSideConditionsProduction(inputCurrStep, inputNextStep)) {
            valid = true;
        }

        return valid;
    }

    private boolean checkUsedProduction(String stackCurrStep, String stackNextStep, GrammarRule productionCurrStep) {
        boolean success = false;

        String leftSide = productionCurrStep.leftSide();
        String rightSide = productionCurrStep.rightSide();

        if (rightSide.equals("epsilon")) {
            String stackCurrStepWOT = stackCurrStep.substring(1);

            if (stackCurrStepWOT.equals(stackNextStep)) {
                success = true;
            }
        } else {
            String firstCharStackCurrStep = String.valueOf(stackCurrStep.charAt(0));
            String firstCharsInputNextStep = stackNextStep.substring(0, rightSide.length());

            if (firstCharStackCurrStep.equals(leftSide) && firstCharsInputNextStep.equals(rightSide)) {
                success = true;
            }
        }

        return success;
    }

    private boolean checkSideConditionsProduction(String stackCurrStep, String stackNextStep) {
        boolean success = false;

        if (stackCurrStep.equals(stackNextStep)) {
            success = true;
        }

        return success;
    }
}
