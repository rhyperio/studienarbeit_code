package de.dhbw.karlsruhe.chomsky.validation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ValidateGrammarCNF {

    public ValidateGrammarCNF() { }

    public boolean validateGrammarIsInCNF(Grammar grammarToCheck) {
        List<GrammarProduction> grammarProductions = Arrays.asList(grammarToCheck.getProductions());
        String[] terminals = grammarToCheck.getTerminals();
        String[] nonTerminals = grammarToCheck.getNonTerminals();
        String startNonTerminal = grammarToCheck.getStartSymbol();
        return this.rightSideExactlyTwoNonTerminals(grammarProductions, terminals, startNonTerminal, nonTerminals) && this.rightSideExactlyOneTerminal(grammarProductions, terminals, startNonTerminal, nonTerminals) && this.maxOneEpsilonProductionNoRightSide(grammarProductions);
    }

    private boolean rightSideExactlyTwoNonTerminals(List<GrammarProduction> grammarProductions, String[] terminals, String startNonTerminal, String[] nonTerminals) {
        boolean valid = false;
        int anzNonTerminals = 0;

        for (GrammarProduction gp : grammarProductions) {
            if (gp.rightSide().length() == 1 && Arrays.stream(terminals).anyMatch(gp.rightSide()::contains) || this.isExceptionalCaseOfStartNonTerminal(gp,startNonTerminal, nonTerminals)) {
                valid = true;
            } else {
                anzNonTerminals = this.getAnzNonTerminalsOnRightSide(gp.rightSide(), nonTerminals);
            }
        }

        if (anzNonTerminals > 2) {
            valid = false;
        }

        if (!valid) {
            return false;
        }

        return true;
    }

    private boolean isExceptionalCaseOfStartNonTerminal(GrammarProduction gp, String startNonTerminal, String[] nonTerminals) {
        if (gp.leftSide().equals(startNonTerminal) && Arrays.asList(nonTerminals).contains(gp.rightSide())) {
            return true;
        }

        return false;
    }

    private int getAnzNonTerminalsOnRightSide(String rightSide, String[] nonTerminals) {
        int anzNonTerminals = 0;

        for (int i = 0; i < rightSide.length(); i++) {
            StringBuilder nonTerminalToCheck = new StringBuilder(String.valueOf(rightSide.charAt(i)));

            if ((nonTerminalToCheck.toString().equals("Z") || nonTerminalToCheck.toString().equals("V")) && (i+1) < rightSide.length()) {
                if (rightSide.charAt(i+1) != '_') {
                    break;
                }

                do {
                    nonTerminalToCheck.append(rightSide.charAt(i + 1));
                    i += 1;

                    if ((i+1) >= rightSide.length()) {
                        break;
                    }
                } while (Character.isDigit(rightSide.charAt(i+1)) || List.of(nonTerminals).contains(String.valueOf(rightSide.charAt(i+1))));
            }

            if (Stream.of(nonTerminals).anyMatch(nonTerminalToCheck.toString()::contains)) {
                anzNonTerminals++;
            }
        }

        return anzNonTerminals;
    }

    private boolean rightSideExactlyOneTerminal(List<GrammarProduction> grammarProductions, String[] terminals, String startNonTerminal, String[] nonTerminals) {
        for (GrammarProduction gp : grammarProductions) {
            if (gp.rightSide().length() == 1) {
                if (Arrays.stream(terminals).noneMatch(gp.rightSide()::contains) && !this.isExceptionalCaseOfStartNonTerminal(gp, startNonTerminal, nonTerminals)) {
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
