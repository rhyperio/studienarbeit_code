package de.dhbw.karlsruhe.top.down.parsing.validation;

import de.dhbw.karlsruhe.models.GrammarRule;
import de.dhbw.karlsruhe.services.GrammarService;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownAcceptor;
import de.dhbw.karlsruhe.models.ParserState;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownStep;

import java.util.Arrays;
import java.util.List;

public class TopDownAcceptorValidation {

    private GrammarService grammarService;
    private TopDownAcceptor tdAcceptor;
    private String wordToPars;

    public TopDownAcceptorValidation(String grammarAsJson) {
        this.grammarService = new GrammarService(grammarAsJson);
    }

    public boolean validateTopDownAcceptor(TopDownAcceptor pTDAcceptor, String pWord) {
        if (pTDAcceptor == null || pWord == null) {
            return false;
        }

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
        ParserState currentState = stepToCheck.getState();
        String stack = stepToCheck.getStack();
        String input = stepToCheck.getReadInput();
        GrammarRule usedProduction = stepToCheck.getUsedProduction();

        if (currentState == ParserState.Z0 && stack.equals("*") && input.equals("") && usedProduction == null) {
            success = true;
        }

        return success;
    }

    private boolean validateLastStep() {
        boolean success = false;

        TopDownStep stepToCheck = this.tdAcceptor.getLastStep();
        ParserState currentState = stepToCheck.getState();
        String stack = stepToCheck.getStack();
        String input = stepToCheck.getReadInput();
        GrammarRule usedProduction = stepToCheck.getUsedProduction();

        if (currentState == ParserState.ZF && stack.equals("*") && input.equals(this.wordToPars) && usedProduction == null) {
            success = true;
        }

        return success;
    }

    private boolean validateSteps() {
        List<TopDownStep> tdSteps = this.tdAcceptor.getTopDownSteps();
        TopDownStep nextStep;
        TopDownStep currStep;
        boolean success = false;

        for (int i=0; i < tdSteps.size(); i++) {
            currStep = tdSteps.get(i);

            if (currStep.equals(this.tdAcceptor.getFirstStep()) || currStep.equals(this.tdAcceptor.getLastStep())) {
                continue;
            }

            nextStep = tdSteps.get(i+1);
            String firstStackChar = String.valueOf(currStep.getStack().charAt(0));

            if (Arrays.stream(this.grammarService.getTerminals()).anyMatch(firstStackChar::equals)) {
                success = this.validateReadStep(currStep, nextStep);
            } else if (Arrays.stream(this.grammarService.getNonTerminals()).anyMatch(firstStackChar::equals)) {
                success = this.validateProductionStep(currStep, nextStep);
            }
            if (!success) {
                return false;
            }
        }

        return success;
    }

    private boolean validateReadStep(TopDownStep tdStep, TopDownStep nextStep) {
        boolean success = false;

        String stackNextStep = nextStep.getStack();
        String stackCurrStep = tdStep.getStack();
        String inputNextStep = nextStep.getReadInput();
        GrammarRule productionCurrStep = tdStep.getUsedProduction();
        ParserState stateCurrStep = tdStep.getState();

        if (stackCurrStep == null || stateCurrStep != ParserState.Z) {
            return false;
        }

        if (this.checkReadInput(stackCurrStep, inputNextStep) && this.checkSideConditionsRead(stackCurrStep, stackNextStep, productionCurrStep)) {
            success = true;
        }

        return success;
    }

    private boolean checkReadInput(String stackCurrStep, String inputNextStep) {
        char firstCharStackCurrStep = stackCurrStep.charAt(0);
        char firstCharInputNextStep = inputNextStep.charAt(inputNextStep.length()-1);

        if (firstCharStackCurrStep == firstCharInputNextStep) {
            return true;
        }

        return false;
    }

    private boolean checkSideConditionsRead(String stackCurrStep, String stackNextStep, GrammarRule productionCurrStep) {
        // validate correct procedure of removing read character and display of stack in next step as well as current production

        char secondCharStackCurrStep = stackCurrStep.charAt(1);
        char firstCharStackNextStep = stackNextStep.charAt(0);

        if (productionCurrStep == null && (secondCharStackCurrStep == firstCharStackNextStep)) {
            return true;
        }

        return false;
    }

    private boolean validateProductionStep(TopDownStep tdStep, TopDownStep nextStep) {
        boolean success = false;

        String stackCurrStep = tdStep.getStack();
        String stackNextStep = nextStep.getStack();
        GrammarRule productionCurrStep = tdStep.getUsedProduction();
        String inputCurrStep = tdStep.getReadInput();
        String inputNextStep = nextStep.getReadInput();
        ParserState stateCurrStep = tdStep.getState();

        if (stackCurrStep == null || productionCurrStep == null || stateCurrStep != ParserState.Z) {
            return false;
        }

        if (this.checkUsedProduction(stackCurrStep, stackNextStep, productionCurrStep) && this.validateNoChangeOfStack(inputCurrStep, inputNextStep)) {
            success = true;
        }

        return success;
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

    private boolean validateNoChangeOfStack(String stackCurrStep, String stackNextStep) {
        if (stackCurrStep.equals(stackNextStep)) {
            return true;
        }

        return false;
    }
}