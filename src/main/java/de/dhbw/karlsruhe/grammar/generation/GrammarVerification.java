package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.GrammarRule;

import java.util.List;

public class GrammarVerification {

    public boolean verifyProductions(List<GrammarRule> pGrammarRulesToCheck) {
        return this.checkLoopInSingleTerminal(pGrammarRulesToCheck);
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
