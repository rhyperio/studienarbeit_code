package de.dhbw.karlsruhe.bottom.up.validation;

import de.dhbw.karlsruhe.bottom.up.models.AcceptorDetailResult;
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

    public AcceptorDetailResult checkAcceptor(BottomUpAcceptor bUAcceptor, String word) {
        if (word == null || bUAcceptor == null)
            return new AcceptorDetailResult(false,"Es sind nicht alle Parameter gegeben.");

        if (!this.grammarService.checkStringOnlyContainsGrammarTerminals(word))
            return new AcceptorDetailResult(false,"Das gegebene Wort ist nicht Teil der Sprache.");

        return isBottomUpAcceptorValid(bUAcceptor, word);
    }

    private AcceptorDetailResult isBottomUpAcceptorValid(BottomUpAcceptor bUAcceptor, String word) {
        List<BottomUpStep> steps = bUAcceptor.getSteps();
        AcceptorDetailResult accDetailResult = isValidFirstStep(steps.get(0), word);
        if (!accDetailResult.isCorrect())
            return accDetailResult;

        AcceptorDetailResult stepResult = new AcceptorDetailResult(true);
        for (int i = 1; i< steps.size()-1 ; i++ ) {
            if (stepResult.isCorrect())
                stepResult = isValidStep(steps.get(i), steps.get(i-1));
        }

        if (!stepResult.isCorrect())
            return stepResult;

        accDetailResult = isValidLastStep(steps.get(steps.size()-1), steps.get(steps.size()-2));
        if (!accDetailResult.isCorrect())
            return accDetailResult;

        return new AcceptorDetailResult(true);
    }


    private AcceptorDetailResult isValidFirstStep(BottomUpStep step, String word) {
        if (step.getState() != ParserState.Z0)
            return new AcceptorDetailResult(false,step, "Der Zustand ist nicht korrekt.");
        if (!StringUtils.equals(step.getStack(), "*"))
            return new AcceptorDetailResult(false,step, "Der Kellerinhalt ist nicht korrekt.");
        if (!StringUtils.equals(step.getRemainingWord(), word))
            return new AcceptorDetailResult(false,step, "Die verbleibende Eingabe ist nicht korrekt.");
        if (step.getProduction() != null)
            return new AcceptorDetailResult(false,step, "Es wurde eine Produktion angegeben.");
        return new AcceptorDetailResult(true);
    }

    private AcceptorDetailResult isValidStep(BottomUpStep step, BottomUpStep priorStep) {
        if (step.getState() != ParserState.Z)
            return new AcceptorDetailResult(false,step, "Der Zustand ist nicht korrekt.");
        if (step.getProduction() != null){
            return isValidReductionStep(step, priorStep);
        } else
            return isValidReadingStep(step, priorStep);
    }

    private AcceptorDetailResult isValidReductionStep(BottomUpStep step, BottomUpStep priorStep) {
        if (!this.grammarService.getGrammarRules().contains(productionService.removeSpaces(step.getProduction())))
            return new AcceptorDetailResult(false,step, "Der Reduktionsschritt enthält keine Produktion.");

        if (!isLeftSideOfProductionExecuted(step) || !isRightSideOfProductionExecuted(step, priorStep))
            return new AcceptorDetailResult(false, step, "Die Reduktion wurde nicht richtig durchgeführt.");

        return new AcceptorDetailResult(true);
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

    private AcceptorDetailResult isValidReadingStep(BottomUpStep step, BottomUpStep priorStep) {
        if (step.getProduction() != null)
            return new AcceptorDetailResult(false, step, "Es wurde eine Produktion für einen Leseschritt angegeben.");
        if (step.getState() != ParserState.Z)
            return new AcceptorDetailResult(false, step, "Der Zustand ist nicht korrekt.");

        boolean correctReading = true;
        if (!isStepRemainingWordEqualPriorStepRemainingWordWithoutFirstCharacter(step, priorStep))
            correctReading = false;
        if (!isStepStackLastCharacterEqualPriorStepFirstRemainingWordCharacter(step, priorStep))
            correctReading = false;
        if (!isStepStackEqualPriorStackPlusOneCharacter(step, priorStep))
            correctReading = false;
        if (!correctReading)
            return new AcceptorDetailResult(false,step, "Der Leseschritt wurde nicht korrekt durchgeführt.");

        return new AcceptorDetailResult(true);
    }


    private AcceptorDetailResult isValidLastStep(BottomUpStep step, BottomUpStep priorStep) {
        if (step.getState() != ParserState.ZF)
            return new AcceptorDetailResult(false, step, "Der Zustand ist nicht korrekt.");
        if (!StringUtils.equals(step.getStack(), "*" + grammarService.getStartSymbol()))
            return new AcceptorDetailResult(false, step, "Der Kellerinhalt ist nicht korrekt.");
        if (!step.getStack().equals(priorStep.getStack()) ||
                    ! priorStep.getState().equals(ParserState.Z) ||
                    ! priorStep.getRemainingWord().isBlank())
            return new AcceptorDetailResult(false, priorStep, "Der vorletzte Schritt ist nicht korrekt.");
        if (!step.getRemainingWord().isBlank())
            return new AcceptorDetailResult(false, step, "Die verbleibende Eingabe ist nicht leer.");
        if (step.getProduction() != null)
            return  new AcceptorDetailResult(false, step, "Es wurde eine Produktion im letzten Schritt angegeben");;
        return new AcceptorDetailResult(true);
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
