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

    List<String> cleanProductions(List<String> generatedProductions) {
        return formatProductions(generatedProductions.stream().distinct().collect(Collectors.toList()));
    }

    boolean isLoop(String startSymbol, List<String> productions, String leftSide, char rightSide) {
        List<String> productionsWithRightSideOnLeft = new ArrayList<>();
        productions.forEach(production -> {
            if (production.substring(production.indexOf('>') + 1 ).contains(leftSide)) {
                productionsWithRightSideOnLeft.add(production);
            }
        });

        for (String production : productionsWithRightSideOnLeft) {
            if (production.charAt(0) == rightSide) {
                return true;
            } else if (production.charAt(0) == startSymbol.charAt(0)) {
                return true;
            } else {
                return isLoop(startSymbol, productions, String.valueOf(production.charAt(0)), rightSide);
            }
        }
        return false;
    }

    String buildProduction(String leftSide, String rightSide) {
        return leftSide + " -> " + rightSide;
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

    private List<String> formatProductions(List<String> generatedProductions) {
        List<String> cleanedProductions = new ArrayList<>();

        for (String currProduction : generatedProductions) {
            char first = currProduction.charAt(0);
            if (isNotAlreadyInList(cleanedProductions, first)) {
                StringBuilder currProductionBuilder = new StringBuilder(currProduction);
                for (String production: generatedProductions) {
                    if (currProduction.equals(production)) {
                        continue;
                    }
                    char second = production.charAt(0);
                    if (first == second) {
                        currProductionBuilder.append(" | ").append(production.substring(production.indexOf('>') + 1));
                    }
                }
                cleanedProductions.add(currProductionBuilder.toString());
            }
        }
        return cleanedProductions;
    }

    private boolean isNotAlreadyInList(List<String> productions, char leftSide) {
        for (String currProd: productions) {
            if (currProd.charAt(0) == leftSide) {
                return false;
            }
        }
        return true;
    }

}
