package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.GrammarProduction;

import java.util.*;

public class GrammarConcatenationVerification {

    public boolean verifyProductions(List<GrammarProduction> grammarRulesToCheck, String[] nonTerminals) {
        boolean valid = this.checkStartSymbolIsMappingToOtherNonTerminals(grammarRulesToCheck, nonTerminals);

        if (!valid) {
            return false;
        } else {
            valid = this.checkLoopInSingleNonTerminal(grammarRulesToCheck);
        }

        if (!valid) {
            return false;
        } else {
            valid = this.checkEveryNonTerminalIsReached(grammarRulesToCheck, nonTerminals);
        }

        return valid;
    }

    public Set<String> getNonTerminatingNonTerminals(Set<GrammarProduction> grammarRulesToCheck, String[] nonTerminals) {
        Set<String> nonTerminatingNonTerminals = new HashSet<>();
        boolean loop;

        for (String currentNonTerminal : nonTerminals) {
            if (this.checkIfNonTerminalLoopsItself(currentNonTerminal, grammarRulesToCheck)) {
                nonTerminatingNonTerminals.add(currentNonTerminal);
            }
        }

        return nonTerminatingNonTerminals;
    }
    private boolean checkIfNonTerminalLoopsItself(String nonTerminal, Set<GrammarProduction> grammarRulesToCheck) {
        List<String> leftSidesOfUsage = new ArrayList<>();
        StringBuilder rightSidesOfNonTerminalToCheck = new StringBuilder();

        for (GrammarProduction gr : grammarRulesToCheck) {
            if (gr.leftSide().equals(nonTerminal)) {
                rightSidesOfNonTerminalToCheck.append(gr.rightSide());
            }

            if (gr.rightSide().contains(nonTerminal)) {
                leftSidesOfUsage.add(gr.leftSide());
            }
        }

        if (leftSidesOfUsage.contains(nonTerminal) || this.checkIfRightSideContainsUsedNonTerminals(rightSidesOfNonTerminalToCheck.toString(), leftSidesOfUsage)) {
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

    private boolean checkStartSymbolIsMappingToOtherNonTerminals(List<GrammarProduction> grammarRulesToCheck, String[] nonTerminals) {
        boolean valid = false;

        String startSymbol = nonTerminals[0];
        List<String> nonTerminalsToCheck = Arrays.stream(nonTerminals).toList();
        nonTerminalsToCheck.remove(0);

        for (GrammarProduction gr : grammarRulesToCheck) {
            if (gr.leftSide().equals(startSymbol)) {
                for (int i = 0; i < gr.rightSide().length(); i++) {
                    if (nonTerminalsToCheck.contains(String.valueOf(gr.rightSide().charAt(i)))) {
                        valid = true;
                    }
                }
            }
        }

        return valid;
    }

    private boolean checkEveryNonTerminalIsReached(List<GrammarProduction> grammarRulesToCheck, String[] nonTerminals) {
        List<String> usedNonTerminals = new ArrayList<>();

        // check if every non Terminal is reached at least once
        for (GrammarProduction gr : grammarRulesToCheck) {
            for (int i = 0; i < gr.rightSide().length(); i++) {
                if (Arrays.asList(nonTerminals).contains(String.valueOf(gr.rightSide().charAt(i)))) {
                    usedNonTerminals.add(String.valueOf(gr.rightSide().charAt(i)));
                }
            }
        }

        return Arrays.stream(nonTerminals).distinct().toList().equals(usedNonTerminals);
    }

    private boolean checkLoopInSingleNonTerminal(List<GrammarProduction> grammarRulesToCheck) {
        boolean valid = false;
        String prevProductionNonTerminal = "";
        String currProductionNonTerminal = "";

        for (int i = 0; i < grammarRulesToCheck.size(); i++) {
            GrammarProduction currGR = grammarRulesToCheck.get(i);
            currProductionNonTerminal = currGR.leftSide();

            if (i != 0 && !valid) {
                break;
            }

            if (i == 0 || !prevProductionNonTerminal.equals(currProductionNonTerminal)) {
                valid = this.checkLeftSideUnequalRightSide(currGR);
            }

            prevProductionNonTerminal = currProductionNonTerminal;
        }

        return valid;
    }

    private boolean checkLeftSideUnequalRightSide(GrammarProduction currGR) {
        return !currGR.leftSide().equals(currGR.rightSide());
    }
}
