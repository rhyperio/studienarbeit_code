package de.dhbw.karlsruhe.bottom.up.validation;

import de.dhbw.karlsruhe.bottom.up.models.BottomUpAcceptor;
import de.dhbw.karlsruhe.bottom.up.models.BottomUpState;
import de.dhbw.karlsruhe.bottom.up.models.BottomUpStep;
import de.dhbw.karlsruhe.services.GrammarService;

import java.util.List;
import java.util.Objects;

public class BottomUpAcceptorValidation {

    private GrammarService grammerService;

    public BottomUpAcceptorValidation(String grammarAsJson) {
        this.grammerService = new GrammarService(grammarAsJson);
    }

    public boolean checkAcceptor(BottomUpAcceptor bUAcceptor, String word) {
        if (word == null)
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
             correctStep = validateStep(steps.get(i), word);
        }
        if (!validateLastStep(steps.get(steps.size()-1), word))
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

    private boolean validateStep(BottomUpStep step, String word) {
        return true;
    }

    private boolean validateLastStep(BottomUpStep step, String word) {
        if (step.getState() != BottomUpState.zf)
            return false;
        if (!Objects.equals(step.getStack(), "*" + grammerService.getStartSymbol()))
            return false;
        if (!Objects.equals(step.getRemainingWord(), ""))
            return false;
        if (step.getProduction() != null)
            return false;
        return true;
    }

}
