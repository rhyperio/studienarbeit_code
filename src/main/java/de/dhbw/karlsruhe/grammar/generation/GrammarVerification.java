package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.GrammarRule;

import java.util.List;

public class GrammarVerification {

    public boolean verifyProductions(List<GrammarRule> pGrammarRulesToCheck) {
        boolean valid = false;
        valid = this.checkStartSymbolIsMappingToOtherNonTerminals(pGrammarRulesToCheck);

        if (!valid) {
            return false;
        } else {
            valid = this.checkLoopInSingleTerminal(pGrammarRulesToCheck);
        }

        if (!valid) {
            return false;
        } else {
            valid = this.checkEveryNonTerminalIsReached(pGrammarRulesToCheck);
        }

        return valid;
    }

    private boolean checkStartSymbolIsMappingToOtherNonTerminals(List<GrammarRule> pGrammarRulesToCheck) {
        boolean valid = false;

        // check if Start Symbol refers to other non terminals if Grammar has more than one non terminal

        return true;
    }

    private boolean checkEveryNonTerminalIsReached(List<GrammarRule> pGrammarRulesToCheck) {
        boolean valid = false;

        // check if every non terminal is reached at least ones

        return true;
    }

    private boolean checkLoopInSingleTerminal(List<GrammarRule> pGrammarRulesToCheck) {
        boolean valid = false;
        String prevProductionNonTerminal = "";
        String currProductionNonTerminal = "";

        for (int i = 0; i < pGrammarRulesToCheck.size(); i++) {
            GrammarRule currGR = pGrammarRulesToCheck.get(i);
            currProductionNonTerminal = currGR.leftSide();

            if (i != 0 && !valid) {
                continue;
            }

            if (i == 0 || !prevProductionNonTerminal.equals(currProductionNonTerminal)) {
                valid = this.checkLeftSideUnequalRightSide(currGR);
            }

            prevProductionNonTerminal = currProductionNonTerminal;
        }

        return valid;
    }

    private boolean checkLeftSideUnequalRightSide(GrammarRule pCurrGR) {
        return !pCurrGR.leftSide().equals(pCurrGR.rightSide());
    }
}
