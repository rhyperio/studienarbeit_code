package de.dhbw.karlsruhe.derivation.leftRight.validation;

import de.dhbw.karlsruhe.derivation.leftRight.models.Derivation;
import de.dhbw.karlsruhe.derivation.leftRight.models.SpecificDerivationValidationDetailResult;
import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;

import java.util.Arrays;
import java.util.List;

public class SpecificDerivationValidation {

    public SpecificDerivationValidationDetailResult checkDerivation(Derivation derivation, Grammar grammar, List<String> derivationList, String word) {
        if (derivationList.size() < 2) {
            return new SpecificDerivationValidationDetailResult(false, "Keine oder zu wenig Ableitungsschritte angegeben");
        }

        if (!checkStartSymbol(derivationList, grammar)) {
            return new SpecificDerivationValidationDetailResult(
                    false,
                    buildGrammarProduction(derivationList.get(0), derivationList.get(1)),
                    "Startsymbol nicht korrekt");
        }

        return checkDerivationSteps(derivation, grammar, derivationList, word);
    }

    private boolean checkStartSymbol(List<String> derivationList, Grammar grammar) {
        return derivationList.get(0).equals(grammar.getStartSymbol());
    }

    private GrammarProduction buildGrammarProduction(String left, String right) {
        return new GrammarProduction(left, right);
    }

    private SpecificDerivationValidationDetailResult checkDerivationSteps(Derivation derivation, Grammar grammar, List<String> derivationList, String word) {
        for (int i = 0; i < derivationList.size() - 1; i++) {
            String left = derivationList.get(i);
            String right = derivationList.get(i + 1);

            if (i == 0) {
                GrammarProduction firstProduction = new GrammarProduction(left, right);
                if (!Arrays.stream(grammar.getProductions()).toList().contains(firstProduction)) {
                    return new SpecificDerivationValidationDetailResult(
                            false,
                            firstProduction,
                            "Ableitungsschritt nicht korrekt");
                }
            } else {
                String nonTerminal = findNonTerminal(derivation, left);
                String newPartOnRightSide = findNewPartOnRightSide(left, right);
            }
        }
        return new SpecificDerivationValidationDetailResult(true);
    }

    private String findNonTerminal(Derivation derivation, String word) {
        if (derivation == Derivation.LEFT) {
            return findFirstNonTerminal(word);
        } else {
            return findLastNonTerminal(word);
        }
    }

    private String findFirstNonTerminal(String word) {
        for (int i = 0; i < word.length(); i++) {
            if (Character.isUpperCase(word.charAt(i))) {
                return String.valueOf(word.charAt(i));
            }
        }
        return null;
    }

    private String findLastNonTerminal(String word) {
        for (int i = word.length() - 1; i >= 0; i--) {
            if (Character.isUpperCase(word.charAt(i))) {
                return String.valueOf(word.charAt(i));
            }
        }
        return null;
    }

    private String findNewPartOnRightSide(String left, String right) {
        // TODO
        return null;
    }

}
