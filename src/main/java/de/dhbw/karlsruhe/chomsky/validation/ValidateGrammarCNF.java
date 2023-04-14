package de.dhbw.karlsruhe.chomsky.validation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.services.GrammarService;

import java.util.Arrays;
import java.util.List;

public class ValidateGrammarCNF {

    public ValidateGrammarCNF() { }

    public boolean validateGrammarIsInCNF(Grammar grammarToCheck) {
        List<GrammarProduction> grammarProductions = Arrays.asList(grammarToCheck.getProductions());
        String[] nonTerminals = grammarToCheck.getNonTerminals();
        String[] terminals = grammarToCheck.getTerminals();
        return this.rightSideExactlyTwoNonTerminals(grammarProductions, nonTerminals) && this.rightSideExactlyOneTerminal(grammarProductions, terminals) && this.maxOneEpsilonProductionNoRightSide(grammarProductions);
    }

    private boolean rightSideExactlyTwoNonTerminals(List<GrammarProduction> grammarProductions, String[] nonTerminals) {
        boolean valid = false;

        for (GrammarProduction gp : grammarProductions) {
            if (gp.rightSide().length() == 1) {
                valid = true;
            } else {
                int loops = 0;
                for (int i = 0; i < gp.rightSide().length(); i++) {
                    StringBuilder nonTerminalToCheck = new StringBuilder(String.valueOf(gp.rightSide().charAt(i)));

                    if (nonTerminalToCheck.toString().equals("Z") || nonTerminalToCheck.toString().equals("V")) {
                        do {
                            nonTerminalToCheck.append(String.valueOf(gp.rightSide().charAt(i + 1)));
                            i += 1;
                        } while (Character.isDigit(gp.rightSide().charAt(i+1)));
                    }

                    valid = Arrays.stream(nonTerminals).anyMatch(nonTerminalToCheck.toString()::contains);

                    if (!valid) {
                        return false;
                    }

                    loops++;

                    if (loops > 2) {
                        return false;
                    }
                }
            }
        }

        return valid;
    }

    private boolean rightSideExactlyOneTerminal(List<GrammarProduction> grammarProductions, String[] terminals) {
        for (GrammarProduction gp : grammarProductions) {
            if (gp.rightSide().length() == 1) {
                if (Arrays.stream(terminals).noneMatch(gp.rightSide()::contains)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean maxOneEpsilonProductionNoRightSide(List<GrammarProduction> grammarProductions) {
        int anzEpsilonProductions = 0;
        String leftSideOfEpsilonProduction = "";

        for (GrammarProduction gp : grammarProductions) {
            if (gp.rightSide().equals("ε")) {
                anzEpsilonProductions ++;
                leftSideOfEpsilonProduction = gp.leftSide();
            }
        }

        if (anzEpsilonProductions > 1) {
            return false;
        }

        if (anzEpsilonProductions == 0) {
            return true;
        } else {
            return this.checkEpsilonProductionIsOnNoRightSide(grammarProductions, leftSideOfEpsilonProduction);
        }
    }

    private boolean checkEpsilonProductionIsOnNoRightSide(List<GrammarProduction> grammarProductions, String leftSideOfEpsilonProduction) {
        for (GrammarProduction gp : grammarProductions) {
            if (gp.rightSide().contains(leftSideOfEpsilonProduction)) {
                return false;
            }
        }

        return true;
    }
}
