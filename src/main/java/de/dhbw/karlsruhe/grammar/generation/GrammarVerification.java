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
        return true;
    }
}
