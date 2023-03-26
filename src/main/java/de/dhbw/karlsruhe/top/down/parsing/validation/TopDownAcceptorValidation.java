package de.dhbw.karlsruhe.top.down.parsing.validation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.services.GrammarService;
import de.dhbw.karlsruhe.top.down.parsing.models.TDAcceptorDetailResult;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownAcceptor;
import de.dhbw.karlsruhe.models.ParserState;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownStep;

import java.util.Arrays;
import java.util.List;

public class TopDownAcceptorValidation {

    private GrammarService grammarService;
    private TopDownAcceptor tdAcceptor;
    private String wordToPars;
    private TDAcceptorDetailResult tdAcceptorDetailResult = new TDAcceptorDetailResult(false);

    public TopDownAcceptorValidation(String grammarAsJson) {
        this.grammarService = new GrammarService(grammarAsJson);
    }
    public TopDownAcceptorValidation(Grammar grammar) {
        this.grammarService = new GrammarService(grammar);
    }

    public TDAcceptorDetailResult validateTopDownAcceptor(TopDownAcceptor pTDAcceptor, String pWord) {
        if (pTDAcceptor == null || pWord == null) {
            if (pTDAcceptor == null) {
                this.tdAcceptorDetailResult.setMessage("Es ist keine Top-down-Syntaxanalyse angegeben!");
            } else {
                this.tdAcceptorDetailResult.setMessage("Es ist kein Wort angegeben!");
            }
            return this.tdAcceptorDetailResult;
        }

        this.tdAcceptor = pTDAcceptor;
        this.wordToPars = pWord;

        if (!this.validateFirstStep() || !this.validateLastStep() || !this.validateSteps()) {
            return tdAcceptorDetailResult;
        }
        return new TDAcceptorDetailResult(true);
    }

    private boolean validateFirstStep() {
        boolean success = false;

        TopDownStep stepToCheck = this.tdAcceptor.getFirstStep();
        ParserState currentState = stepToCheck.getState();
        String stack = stepToCheck.getStack();
        String input = stepToCheck.getReadInput();
        GrammarProduction usedProduction = stepToCheck.getUsedProduction();

        if (currentState == ParserState.Z0 && stack.equals("*") && input.equals("") && usedProduction == null) {
            success = true;
        } else {
            this.tdAcceptorDetailResult.setWrongStep(stepToCheck);
            if (currentState != ParserState.Z0) {
                this.tdAcceptorDetailResult.setMessage("Der Zustand im ersten Schritt muss z0 sein!");
            } else if (!stack.equals("*")) {
                this.tdAcceptorDetailResult.setMessage("Der Kellerinhalt muss bei dem ersten Schritt leer sein!");
            } else if (!input.equals("")) {
                this.tdAcceptorDetailResult.setMessage("Die gelesene Eingabe muss bei dem ersten Schritt leer sein!");
            } else if (usedProduction != null) {
                this.tdAcceptorDetailResult.setMessage("Es darf keine Produktion bei dem ersten Schritt angewendet werden!");
            }
        }

        return success;
    }

    private boolean validateLastStep() {
        boolean success = false;

        TopDownStep stepToCheck = this.tdAcceptor.getLastStep();
        ParserState currentState = stepToCheck.getState();
        String stack = stepToCheck.getStack();
        String input = stepToCheck.getReadInput();
        GrammarProduction usedProduction = stepToCheck.getUsedProduction();

        TopDownStep prevLastStep = this.tdAcceptor.getSecondLastStep();
        ParserState prevState = prevLastStep.getState();
        String prevStack = prevLastStep.getStack();
        String prevInput = prevLastStep.getReadInput();
        GrammarProduction prevUsedProduction = prevLastStep.getUsedProduction();

        if (currentState == ParserState.ZF && stack.equals("*") && input.equals(this.wordToPars) && usedProduction == null) {
            if (prevState == ParserState.Z && prevStack.equals(stack) && prevInput.equals(input) && prevUsedProduction == null) {
                success = true;
            } else {
                this.tdAcceptorDetailResult.setWrongStep(prevLastStep);
                if (prevState != ParserState.Z) {
                    this.tdAcceptorDetailResult.setMessage("Der Zustand ist falsch!");
                } else if (!prevStack.equals(stack)) {
                    this.tdAcceptorDetailResult.setMessage("Der Keller stimmt nicht mit dem letzten Schritt überein!");
                } else if (!prevInput.equals(input)) {
                    this.tdAcceptorDetailResult.setMessage("Die gelesene Eingabe stimmt nicht mit dem letzten Schritt überein!");
                } else {
                    this.tdAcceptorDetailResult.setMessage("Es darf keine Produktion angewendet werden!");
                }
            }
        } else {
            this.tdAcceptorDetailResult.setWrongStep(stepToCheck);
            if (currentState != ParserState.ZF) {
                this.tdAcceptorDetailResult.setMessage("Der Zustand ist falsch!");
            } else if (!stack.equals("*")) {
                this.tdAcceptorDetailResult.setMessage("Der Keller ist nicht leer!");
            } else if (!input.equals(this.wordToPars)) {
                this.tdAcceptorDetailResult.setMessage("Die gelesene Eingabe stimmt nicht mit dem abzuleitenden Wort überein!");
            } else if (usedProduction != null) {
                this.tdAcceptorDetailResult.setMessage("Es darf keine Produktion angewendet werden!");
            }
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

            if (currStep.equals(this.tdAcceptor.getFirstStep()) || currStep.equals(this.tdAcceptor.getLastStep()) || currStep.equals(this.tdAcceptor.getSecondLastStep())) {
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
                this.tdAcceptorDetailResult.setWrongStep(currStep);
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
        GrammarProduction productionCurrStep = tdStep.getUsedProduction();
        ParserState stateCurrStep = tdStep.getState();

        if (stackCurrStep == null) {
            this.tdAcceptorDetailResult.setMessage("Der Keller darf nicht leer sein!");
            return false;
        } else if (stateCurrStep != ParserState.Z) {
            this.tdAcceptorDetailResult.setMessage("Der angegebene Zustand ist falsch!");
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

        this.tdAcceptorDetailResult.setMessage("Der Leseschritt wurde nicht richtig durchgeführt!");
        return false;
    }

    private boolean checkSideConditionsRead(String stackCurrStep, String stackNextStep, GrammarProduction productionCurrStep) {
        // validate correct procedure of removing read character and display of stack in next step as well as current production

        char secondCharStackCurrStep = stackCurrStep.charAt(1);
        char firstCharStackNextStep = stackNextStep.charAt(0);

        if (productionCurrStep == null) {
            return true;
        } else {
            this.tdAcceptorDetailResult.setMessage("Es darf keine Produktion angewendet werden!");
        }

        if (secondCharStackCurrStep == firstCharStackNextStep) {
            return true;
        } else {
            this.tdAcceptorDetailResult.setMessage("Das gelesene Terminal wurde nicht korrekt gelesen!");
        }

        return false;
    }

    private boolean validateProductionStep(TopDownStep tdStep, TopDownStep nextStep) {
        boolean success = false;

        String stackCurrStep = tdStep.getStack();
        String stackNextStep = nextStep.getStack();
        GrammarProduction productionCurrStep = tdStep.getUsedProduction();
        String inputCurrStep = tdStep.getReadInput();
        String inputNextStep = nextStep.getReadInput();
        ParserState stateCurrStep = tdStep.getState();

        if (stackCurrStep == null) {
            this.tdAcceptorDetailResult.setMessage("Der Keller darf nicht leer sein!");
            return false;
        } else if (productionCurrStep == null) {
            this.tdAcceptorDetailResult.setMessage("Es ist keine Produktion angegeben!");
            return false;
        } else if (stateCurrStep != ParserState.Z) {
            this.tdAcceptorDetailResult.setMessage("Der Zutand ist falsch!");
            return false;
        }

        if (this.checkUsedProduction(stackCurrStep, stackNextStep, productionCurrStep) && this.validateNoChangeOfStack(inputCurrStep, inputNextStep)) {
            success = true;
        }

        return success;
    }

    private boolean checkUsedProduction(String stackCurrStep, String stackNextStep, GrammarProduction productionCurrStep) {
        boolean success = false;

        if (!this.grammarService.getGrammarRules().contains(productionCurrStep)) {
            this.tdAcceptorDetailResult.setMessage("Die angegebene Produktion ist nicht in der Grammatik enthalten!");
            return false;
        }

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

        if (!success) {
            this.tdAcceptorDetailResult.setMessage("Fehler bei dem Produktionsschritt!");
        }

        return success;
    }

    private boolean validateNoChangeOfStack(String stackCurrStep, String stackNextStep) {
        if (stackCurrStep.equals(stackNextStep)) {
            return true;
        }

        this.tdAcceptorDetailResult.setMessage("Der Kellerinhalt darf sich beim Produktionsschritt nicht ändern!");
        return false;
    }
}
