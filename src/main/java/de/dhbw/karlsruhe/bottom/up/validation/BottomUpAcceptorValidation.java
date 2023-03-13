package de.dhbw.karlsruhe.bottom.up.validation;

import de.dhbw.karlsruhe.bottom.up.models.BottomUpAcceptor;
import de.dhbw.karlsruhe.bottom.up.models.BottomUpStep;
import de.dhbw.karlsruhe.models.ParserState;
import de.dhbw.karlsruhe.services.GrammarService;
import de.dhbw.karlsruhe.services.ProductionService;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class BottomUpAcceptorValidation {
    private final GrammarService grammarService;
    private final ProductionService productionService;
    public BottomUpAcceptorValidation(String grammarAsJson) {
        this.grammarService = new GrammarService(grammarAsJson);
        this.productionService = new ProductionService();
    }

    public boolean checkAcceptor(BottomUpAcceptor bUAcceptor, String word) {
        if (word == null || bUAcceptor == null)
            return false;

        if (!this.grammarService.checkStringOnlyContainsGrammarTerminals(word))
            return false;

        return isBottomUpAcceptorValid(bUAcceptor, word);
    }

    private boolean isBottomUpAcceptorValid(BottomUpAcceptor bUAcceptor, String word) {
        List<BottomUpStep> steps = bUAcceptor.getSteps();
        if (!isValidFirstStep(steps.get(0),word))
            return false;

        boolean correctStep = true;
        for (int i = 1; i< steps.size()-1 ; i++ ) {
            if (correctStep)
             correctStep = isValidStep(steps.get(i), steps.get(i-1));
        }
        if (!isValidLastStep(steps.get(steps.size()-1), steps.get(steps.size()-2)))
            return false;

        return correctStep;
    }


    private boolean isValidFirstStep(BottomUpStep step, String word) {
        if (step.getState() != ParserState.Z0)
            return false;
        if (!StringUtils.equals(step.getStack(), "*"))
            return false;
        if (!StringUtils.equals(step.getRemainingWord(), word))
            return false;
        if (step.getProduction() != null)
            return false;
        return true;
    }

    private boolean isValidStep(BottomUpStep step, BottomUpStep priorStep) {
        if (step.getState() != ParserState.Z)
            return false;
        if (step.getProduction() != null){
            return isValidReductionStep(step, priorStep);
        } else
            return isValidReadingStep(step, priorStep);
    }

    private boolean isValidReductionStep(BottomUpStep step, BottomUpStep priorStep) {
        if (!this.grammarService.getGrammarRules().contains(productionService.removeSpaces(step.getProduction())))
            return false;

        return isLeftSideOfProductionExecuted(step)
                && isRightSideOfProductionExecuted(step, priorStep);
    }

    private boolean isLeftSideOfProductionExecuted(BottomUpStep step) {
        return step.getProduction().leftSide().equals(step.getStack().substring(step.getStack().length()-1));
    }

    private boolean isRightSideOfProductionExecuted(BottomUpStep step, BottomUpStep priorStep) {
        boolean rightSideValidation;
        String stepProductionRightSideWithoutSpaces = step.getProduction().rightSide().replaceAll("\\s+","");

        if (step.getProduction().rightSide().equals("epsilon")){
            rightSideValidation = priorStep.getStack().equals(step.getStack().substring(0, step.getStack().length()-1));
        } else {
            rightSideValidation = stepProductionRightSideWithoutSpaces.equals(priorStep.getStack().substring(priorStep.
                    getStack().length()-stepProductionRightSideWithoutSpaces.length()));
        }
        return rightSideValidation;
    }

    private boolean isValidReadingStep(BottomUpStep step, BottomUpStep priorStep) {
        if (step.getProduction() != null)
            return false;
        if (step.getState() != ParserState.Z)
            return false;
        if (!isStepRemainingWordEqualPriorStepRemainingWordWithoutFirstCharacter(step, priorStep))
            return false;
        if (!isStepStackLastCharacterEqualPriorStepFirstRemainingWordCharacter(step, priorStep))
            return false;
        if (!isStepStackEqualPriorStackPlusOneCharacter(step, priorStep))
            return false;
        return true;
    }


    private boolean isValidLastStep(BottomUpStep step, BottomUpStep priorStep) {
        if (step.getState() != ParserState.ZF)
            return false;
        if (!StringUtils.equals(step.getStack(), "*" + grammarService.getStartSymbol()))
            return false;
        if (!step.getStack().equals(priorStep.getStack()) ||
                    ! priorStep.getState().equals(ParserState.Z) ||
                    ! priorStep.getRemainingWord().isBlank())
            return false;
        if (!step.getRemainingWord().isBlank())
            return false;
        if (step.getProduction() != null)
            return false;
        return true;
    }

    private boolean isStepRemainingWordEqualPriorStepRemainingWordWithoutFirstCharacter(BottomUpStep step, BottomUpStep priorStep) {
        return step.getRemainingWord().equals(priorStep.getRemainingWord().substring(1));
    }

    private boolean isStepStackLastCharacterEqualPriorStepFirstRemainingWordCharacter(BottomUpStep step, BottomUpStep priorStep) {
        return step.getStack().substring(step.getStack().length()-1).equals(priorStep.getRemainingWord().substring(0,1));
    }

    private boolean isStepStackEqualPriorStackPlusOneCharacter(BottomUpStep step, BottomUpStep priorStep) {
        return step.getStack().substring(0, step.getStack().length()-1).equals(priorStep.getStack());
    }

}
