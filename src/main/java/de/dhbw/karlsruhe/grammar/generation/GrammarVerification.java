package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.GrammarRule;

import java.util.List;

public class GrammarVerification {

    public boolean verifyProductions(List<GrammarRule> pGrammarRulesToCheck) {
        boolean valid = true;

        valid = this.checkLoopInSingleTerminal(pGrammarRulesToCheck);

        return valid;
    }

    private boolean checkLoopInSingleTerminal(List<GrammarRule> pGrammarRulesToCheck) {
        boolean valid = false;
        String prevProductionTerminal = "";
        String currProductionTerminal = "";

        for (int i = 0; i < pGrammarRulesToCheck.size(); i++) {
            GrammarRule currGR = pGrammarRulesToCheck.get(i);
            currProductionTerminal = currGR.leftSide();

            if (i != 0 && !valid) {
                continue;
            }

            if (i == 0 || !prevProductionTerminal.equals(currProductionTerminal)) {
                valid = this.checkEqualityOfLeftAndRight(currGR);
            }

            prevProductionTerminal = currProductionTerminal;
        }

        return valid;
    }

    private boolean checkEqualityOfLeftAndRight(GrammarRule pCurrGR) {
        if (!pCurrGR.leftSide().equals(pCurrGR.rightSide())) {
            return true;
        }

        return false;
    }
}
