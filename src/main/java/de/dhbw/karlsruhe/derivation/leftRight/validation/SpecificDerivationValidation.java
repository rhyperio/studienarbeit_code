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
                    buildStep(derivationList.get(0), derivationList.get(1)),
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
                            buildStep(left, right),
                            "Ableitungsschritt nicht korrekt");
                }
            } else {
                SpecificDerivationValidationDetailResult result = checkForCorrectSubstitution(derivation, grammar, left, right);
                if (result != null) {
                    return result;
                }
            }
        }
        return new SpecificDerivationValidationDetailResult(true);
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

    private SpecificDerivationValidationDetailResult checkForCorrectSubstitution(Derivation derivation, Grammar grammar, String left, String right) {

        String nonTerminal = derivation == Derivation.LEFT ? findFirstNonTerminal(left) : findLastNonTerminal(left);
        List<GrammarProduction> possibleProductions = Arrays.stream(grammar.getProductions())
            .filter(production -> production.leftSide().equals(nonTerminal)).toList();

        for (GrammarProduction currProduction : possibleProductions) {
            String substitution = substituteNonTerminal(derivation, nonTerminal, left, currProduction.rightSide());
            if (substitution.equals(right)) {
                return null;
            }
        }
        return new SpecificDerivationValidationDetailResult(false, buildStep(left, right), "Ableitungsschritt ist nicht korrekt");
    }

    private String substituteNonTerminal(Derivation derivation, String nonTerminal, String left, String rightSideFromGrammarProduction) {
        rightSideFromGrammarProduction = rightSideFromGrammarProduction.replaceAll("ε", "");
        if (derivation == Derivation.LEFT) {
            return left.replaceFirst(nonTerminal, rightSideFromGrammarProduction);
        } else {
            return replaceLast(left, nonTerminal, rightSideFromGrammarProduction);
        }
    }

    private String replaceLast(String string, String from, String to) {
        int lastIndex = string.lastIndexOf(from);
        if (lastIndex < 0)
            return string;
        String tail = string.substring(lastIndex).replaceFirst(from, to);
        return string.substring(0, lastIndex) + tail;
    }

    private String buildStep(String first, String second) {
        return first + "->" + second;
    }

}
