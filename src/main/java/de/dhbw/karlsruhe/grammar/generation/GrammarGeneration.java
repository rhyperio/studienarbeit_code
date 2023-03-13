package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.Grammar;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.dhbw.karlsruhe.models.GrammarProduction;
import org.apache.commons.lang3.RandomStringUtils;

public abstract class GrammarGeneration {

    List<String> terminals = new ArrayList<>();
    List<String> nonTerminals = new ArrayList<>();
    String startSymbol;

    public abstract Grammar generateGrammar();
    protected abstract List<GrammarProduction>  generateProductions();

    List<GrammarProduction> cleanProductions(List<GrammarProduction> generatedProductions) {
        return generatedProductions.stream().distinct().collect(Collectors.toList());
    }

    boolean isLoop(String startSymbol, List<GrammarProduction> productions, String leftSide, char rightSide) {
        List<GrammarProduction> productionsWithRightSideOnLeft = new ArrayList<>();
        productions.forEach(production -> {
            if (production.rightSide().contains(leftSide)) {
                productionsWithRightSideOnLeft.add(production);
            }
        });

        for (GrammarProduction production : productionsWithRightSideOnLeft) {
            if (production.leftSide().charAt(0) == rightSide) {
                return true;
            } else if (production.leftSide().charAt(0) == startSymbol.charAt(0) || production.leftSide().charAt(0) == leftSide.charAt(0)) {
                return true;
            } else {
                return isLoop(startSymbol, productions, String.valueOf(production.leftSide().charAt(0)), rightSide);
            }
        }
        return false;
    }

    GrammarProduction buildProduction(String leftSide, String rightSide) {
        return new GrammarProduction(leftSide, rightSide);
    }

    List<String> generateTerminals() {
        List<String> terminals = new ArrayList<>();
        while (terminals.size() < 5) {
            String terminal = RandomStringUtils.randomAlphabetic(1).toLowerCase();
            if (!terminals.contains(terminal)) {
                terminals.add(terminal);
            }
        }
        return terminals;
    }

    List<String> generateNonTerminals() {
        List<String> generatedNonTerminals = new ArrayList<>();
        while (generatedNonTerminals.size() < 5) {
            String nonTerminal = RandomStringUtils.randomAlphabetic(1).toUpperCase();
            if (!generatedNonTerminals.contains(nonTerminal)) {
                generatedNonTerminals.add(nonTerminal);
            }
        }
        return generatedNonTerminals;
    }

}
