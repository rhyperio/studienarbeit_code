package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.GrammarRule;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;

public class GrammarVerification {

    public boolean verifyProductions(List<GrammarRule> pGrammarRulesToCheck, String[] pNonTerminals) {
        boolean valid = false;
        valid = this.checkStartSymbolIsMappingToOtherNonTerminals(pGrammarRulesToCheck, pNonTerminals);

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

    private boolean checkStartSymbolIsMappingToOtherNonTerminals(List<GrammarRule> pGrammarRulesToCheck, String[] pNonTerminals) {
        boolean valid = false;

        String prevProductionNonTerminal = "";
        String currProductionNonTerminal = "";

        String startSymbol = pGrammarRulesToCheck.get(0).leftSide();

        for (GrammarRule gr : pGrammarRulesToCheck) {
            if (gr.leftSide().equals(startSymbol)) {
                for (int i = 0; i < gr.rightSide().length(); i++) {
                    if (Arrays.stream(pNonTerminals).anyMatch(String.valueOf(gr.rightSide().charAt(i))::equals)) {
                        valid = true;
                    }
                }
            }
        }

        return valid;
    }

    private boolean checkEveryNonTerminalIsReached(List<GrammarRule> pGrammarRulesToCheck) {
        boolean valid = false;

        // check if every non Terminal is reached at least ones

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
