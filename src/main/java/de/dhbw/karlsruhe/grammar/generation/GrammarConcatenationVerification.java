package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.GrammarProduction;

import java.util.*;

public class GrammarConcatenationVerification {

    public boolean verifyProductions(List<GrammarProduction> pGrammarRulesToCheck, String[] pNonTerminals) {
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
            valid = this.checkEveryNonTerminalIsReached(pGrammarRulesToCheck, pNonTerminals);
        }

        return valid;
    }

    public Set<String> getNonTerminatingNonTerminals(Set<GrammarProduction> pGrammarRulesToCheck, String[] pNonTerminals) {
        Set<String> nonTerminatingNonTerminals = new HashSet<>();
        boolean loop;

        for (String currentNonTerminal : pNonTerminals) {
            loop = this.checkIfNonTerminalLoopsItself(currentNonTerminal, pGrammarRulesToCheck);

            if (loop) {
                nonTerminatingNonTerminals.add(currentNonTerminal);
            }
        }

        return nonTerminatingNonTerminals;
    }
    private boolean checkIfNonTerminalLoopsItself(String pNonTerminal, Set<GrammarProduction> pGrammarRulesToCheck) {
        List<String> leftSidesOfUsage = new ArrayList<>();
        String rightSidesOfNonTerminalToCheck = "";

        for (GrammarProduction gr : pGrammarRulesToCheck) {
            if (gr.leftSide().equals(pNonTerminal)) {
                rightSidesOfNonTerminalToCheck += gr.rightSide();
            }

            if (gr.rightSide().contains(pNonTerminal)) {
                leftSidesOfUsage.add(gr.leftSide());
            }
        }

        if (leftSidesOfUsage.contains(pNonTerminal) || this.checkIfRightSideContainsUsedNonTerminals(rightSidesOfNonTerminalToCheck, leftSidesOfUsage)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkIfRightSideContainsUsedNonTerminals(String rightSidesOfNonTerminalToCheck, List<String> leftSidesOfUsage) {
        for (int i = 0; i < leftSidesOfUsage.size(); i++) {
            if (rightSidesOfNonTerminalToCheck.contains(leftSidesOfUsage.get(i))) {
                return true;
            }
        }

        return false;
    }

    private boolean checkStartSymbolIsMappingToOtherNonTerminals(List<GrammarProduction> pGrammarRulesToCheck, String[] pNonTerminals) {
        boolean valid = false;

        String prevProductionNonTerminal = "";
        String currProductionNonTerminal = "";

        String startSymbol = pGrammarRulesToCheck.get(0).leftSide();

        for (GrammarProduction gr : pGrammarRulesToCheck) {
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

    private boolean checkEveryNonTerminalIsReached(List<GrammarProduction> pGrammarRulesToCheck, String[] pNonTerminals) {
        List<String> usedNonTerminals = new ArrayList<>();

        // check if every non Terminal is reached at least ones
        for (GrammarProduction gr : pGrammarRulesToCheck) {
            for (int i = 0; i < gr.rightSide().length(); i++) {
                if (Arrays.stream(pNonTerminals).anyMatch(String.valueOf(gr.rightSide().charAt(i))::equals)) {
                    usedNonTerminals.add(String.valueOf(gr.rightSide().charAt(i)));
                }
            }
        }

        return new HashSet<>(Arrays.stream(pNonTerminals).toList()).equals(new HashSet<>(usedNonTerminals));
    }

    private boolean checkLoopInSingleTerminal(List<GrammarProduction> pGrammarRulesToCheck) {
        boolean valid = false;
        String prevProductionNonTerminal = "";
        String currProductionNonTerminal = "";

        for (int i = 0; i < pGrammarRulesToCheck.size(); i++) {
            GrammarProduction currGR = pGrammarRulesToCheck.get(i);
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

    private boolean checkLeftSideUnequalRightSide(GrammarProduction pCurrGR) {
        return !pCurrGR.leftSide().equals(pCurrGR.rightSide());
    }
}
