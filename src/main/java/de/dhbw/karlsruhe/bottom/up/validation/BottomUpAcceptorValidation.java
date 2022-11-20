package de.dhbw.karlsruhe.bottom.up.validation;

import de.dhbw.karlsruhe.bottom.up.models.BottomUpAcceptor;
import de.dhbw.karlsruhe.bottom.up.models.BottomUpState;
import de.dhbw.karlsruhe.bottom.up.models.BottomUpStep;
import de.dhbw.karlsruhe.services.GrammarService;

import java.util.List;
import java.util.Objects;

public class BottomUpAcceptorValidation {

    private final GrammarService grammarService;

    public BottomUpAcceptorValidation(String grammarAsJson) {
        this.grammarService = new GrammarService(grammarAsJson);
    }

    public boolean checkAcceptor(BottomUpAcceptor bUAcceptor, String word) {
        if (word == null || bUAcceptor == null)
            return false;
        if (!validateSteps(bUAcceptor,word))
            return false;
        return true;
    }

    private boolean validateSteps(BottomUpAcceptor bUAcceptor, String word) {
        List<BottomUpStep> steps = bUAcceptor.getSteps();
        if (!validateFirstStep(steps.get(0),word))
            return false;

        boolean correctStep = true;
        for (int i = 1; i< steps.size()-1 ; i++ ) {
            if (correctStep)
             correctStep = validateStep(steps.get(i), steps.get(i-1));
        }
        if (!validateLastStep(steps.get(steps.size()-1)))
            return false;

        return correctStep;
    }


    private boolean validateFirstStep(BottomUpStep step, String word) {
        if (step.getState() != BottomUpState.z0)
            return false;
        if (!Objects.equals(step.getStack(), "*"))
            return false;
        if (!Objects.equals(step.getRemainingWord(), word))
            return false;
        if (step.getProduction() != null)
            return false;
        return true;
    }

    private boolean validateStep(BottomUpStep step, BottomUpStep priorStep) {
        if (step.getState() != BottomUpState.z)
            return false;
        if (step.getProduction() != null){
            if (!validateReductionStep(step, priorStep))
                return false;
        }else if (!validateReadingStep(step, priorStep)) {
            return false;
        }
        return true;
    }

    private boolean validateReductionStep(BottomUpStep step, BottomUpStep priorStep) {
        boolean rightSideValidation;
        String stepProductionRightSideWithoutSpaces = step.getProduction().rightSide().replaceAll("\\s+","");

        if (step.getProduction().rightSide().equals("epsilon")){
            rightSideValidation = priorStep.getStack().equals(step.getStack().substring(0,step.getStack().length()-1));
        } else {
            rightSideValidation = stepProductionRightSideWithoutSpaces.equals(priorStep.getStack().substring(priorStep.getStack().
                    length()-stepProductionRightSideWithoutSpaces.length()));
        }
        return step.getProduction().leftSide().equals(step.getStack().substring(step.getStack().length()-1))
                && rightSideValidation;
    }

    private boolean validateReadingStep(BottomUpStep step, BottomUpStep priorStep) {
        if (step.getProduction() != null)
            return false;
        if (step.getState() != BottomUpState.z)
            return false;
        if (!step.getRemainingWord().equals(priorStep.getRemainingWord().substring(1)) ||
                !step.getStack().substring(step.getStack().length()-1).equals
                (priorStep.getRemainingWord().substring(0,1)) )
            return false;
        return true;
    }

    private boolean validateLastStep(BottomUpStep step) {
        if (step.getState() != BottomUpState.zf)
            return false;
        if (!Objects.equals(step.getStack(), "*" + grammarService.getStartSymbol()))
            return false;
        if (!Objects.equals(step.getRemainingWord(), ""))
            return false;
        if (step.getProduction() != null)
            return false;
        return true;
    }

}
