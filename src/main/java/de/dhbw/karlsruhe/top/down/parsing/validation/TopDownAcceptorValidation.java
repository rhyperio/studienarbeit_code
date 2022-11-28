package de.dhbw.karlsruhe.top.down.parsing.validation;

import de.dhbw.karlsruhe.models.GrammarRule;
import de.dhbw.karlsruhe.services.GrammarService;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownAcceptor;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownState;
import de.dhbw.karlsruhe.top.down.parsing.models.TopDownStep;

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
        if (stepToCheck.getNewState().compareTo(TopDownState.Z0) == 0) {
            if (stepToCheck.getNewStack().compareTo("*") == 0) {
                if (stepToCheck.getReadInput().compareTo("") == 0) {
                    success = true;
                }
            }
        }

        return success;
    }

    private boolean validateLastStep() {
        boolean success = false;

        return success;
    }

    private boolean validateSteps() {
        boolean success = false;

        return success;
    }
}
