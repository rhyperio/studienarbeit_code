package de.dhbw.karlsruhe.chomsky.validation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.services.GrammarService;

import java.util.Arrays;
import java.util.List;

public class ValidateGrammarCNF {

    public boolean validateGrammarIsInCNF(Grammar grammarToCheck) {
        List<GrammarProduction> grammarProductions = Arrays.asList(grammarToCheck.getProductions());
        String[] nonTerminals = grammarToCheck.getNonTerminals();
        return this.rightSideExactlyTwoNonTerminals(grammarProductions, nonTerminals) && this.rightSideExactlyOneTerminal(grammarProductions) && this.maxOneEpsilonProductionNoRightSide(grammarProductions);
    }

    private boolean rightSideExactlyTwoNonTerminals(List<GrammarProduction> grammarProductions, String[] nonTerminals) {
        boolean valid = false;

        for (GrammarProduction gp : grammarProductions) {
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
            }

        }

        return valid;
    }

    private boolean rightSideExactlyOneTerminal(List<GrammarProduction> grammarProductions) {
        for (GrammarProduction gp : grammarProductions) {
            if (gp.rightSide().length() == 1) {
                if (!Character.isLowerCase(gp.rightSide().charAt(0))) {
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

        return this.checkEpsilonProductionIsOnNoRightSide(grammarProductions, leftSideOfEpsilonProduction);
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
