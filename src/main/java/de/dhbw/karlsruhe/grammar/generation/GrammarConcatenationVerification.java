package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.GrammarProduction;

import java.util.*;
import java.util.stream.Collectors;

public class GrammarConcatenationVerification {

    public boolean verifyProductions(List<GrammarProduction> grammarRulesToCheck, String[] nonTerminals, String[] terminals) {
        boolean valid = this.checkStartSymbolIsMappingToOtherNonTerminals(grammarRulesToCheck, nonTerminals);

        if (!valid) {
            return false;
        } else {
            valid = this.checkLoopInNonTerminal(grammarRulesToCheck, terminals);
        }

        if (!valid) {
            return false;
        } else {
            valid = this.checkEveryNonTerminalIsReached(grammarRulesToCheck, nonTerminals);
        }

        if (!valid) {
            return false;
        } else {
            valid = this.checkEveryTerminalIsReached(grammarRulesToCheck, terminals);
        }

        return valid;
    }

    public Set<String> getNonTerminatingNonTerminals(Set<GrammarProduction> grammarRulesToCheck, String[] nonTerminals) {
        Set<String> nonTerminatingNonTerminals = new HashSet<>();

        for (String currentNonTerminal : nonTerminals) {
            if (this.checkIfNonTerminalLoopsItself(currentNonTerminal, grammarRulesToCheck)) {
                nonTerminatingNonTerminals.add(currentNonTerminal);
            }
        }

        return nonTerminatingNonTerminals;
    }

    public Set<String> getNonReachableNonTerminalsFromStartSymbol(Set<GrammarProduction> grammarRulesToCheck, String[] nonTerminals, String startSymbol) {
        Set<String> reachableNonTerminals = new HashSet<>();
        reachableNonTerminals.add(startSymbol);

        for(int i = 0; i < nonTerminals.length; i++) {
            for(GrammarProduction gp : grammarRulesToCheck) {
                if (reachableNonTerminals.contains(gp.leftSide())) {
                    for(int j = 0; j < gp.rightSide().length(); j++) {
                        if (Arrays.stream(nonTerminals).anyMatch(String.valueOf(gp.rightSide().charAt(j))::contains)) {
                            reachableNonTerminals.add(String.valueOf(gp.rightSide().charAt(j)));
                        }
                    }
                }
            }
        }

        return Arrays.stream(nonTerminals).filter(element -> !reachableNonTerminals.contains(element)).collect(Collectors.toSet());
    }

    private boolean checkIfNonTerminalLoopsItself(String nonTerminal, Set<GrammarProduction> grammarRulesToCheck) {
        List<String> leftSidesOfUsage = new ArrayList<>();
        StringBuilder rightSidesOfNonTerminalToCheck = new StringBuilder();

        for (GrammarProduction gr : grammarRulesToCheck) {
            if (gr.leftSide().equals(nonTerminal)) {
                rightSidesOfNonTerminalToCheck.append(gr.rightSide());
            }

            if (gr.rightSide().contains(nonTerminal) || this.checkIfRightSideContainsUsedNonTerminals(gr.rightSide(), leftSidesOfUsage)) {
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
        List<String> nonTerminalsToCheck = new ArrayList<>(Arrays.stream(nonTerminals).toList());
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

    private boolean checkLoopInNonTerminal(List<GrammarProduction> grammarRulesToCheck, String[] terminals) {
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
                valid = this.checkLeftSideUnequalRightSide(currGR, grammarRulesToCheck, terminals);
            }

            prevProductionNonTerminal = currProductionNonTerminal;
        }

        return valid;
    }

    private boolean checkLeftSideUnequalRightSide(GrammarProduction currGR, List<GrammarProduction> grammarRulesToCheck, String[] terminals) {
        if (currGR.leftSide().equals(currGR.rightSide())) {
            return false;
        } else if (currGR.rightSide().contains(currGR.leftSide())) {
            return leftSideProducesTerminalOnly(currGR.leftSide(), grammarRulesToCheck, terminals);
        }

        return true;
    }

    private boolean leftSideProducesTerminalOnly(String leftSide, List<GrammarProduction> grammarRulesToCheck, String[] terminals) {
        for (GrammarProduction gr : grammarRulesToCheck) {
            if (gr.leftSide().equals(leftSide)) {
                if (Arrays.asList(terminals).contains(gr.rightSide())) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean checkEveryTerminalIsReached(List<GrammarProduction> grammarRulesToCheck, String[] terminals) {
        boolean reached;

        for (String terminal : terminals) {
            reached = false;
            for (GrammarProduction gr : grammarRulesToCheck) {
                if (gr.rightSide().contains(terminal)) {
                    reached = true;
                    break;
                }
            }
            if (!reached) {
                return false;
            }
        }

        return true;
    }
}
