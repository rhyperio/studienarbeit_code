package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarRule;
import de.dhbw.karlsruhe.services.GrammarService;

import java.util.List;

public class GrammarGeneration {
    private GrammarService grammarService;
    private List<GrammarRule> grammarRules;
    private Grammar generatedGrammar;

    public GrammarGeneration() {

    }

    private void grammarFromJson(String grammarAsJson) {
        this.grammarService = new GrammarService(grammarAsJson);
        this.grammarRules = this.grammarService.getGrammarRules();
    }

    private boolean generateGrammar(int anzTerminals, int anzNonTerminals) {
        boolean success = false;

        String valuesTerminals = "abcdefghijklmnopqrstuvwxyz";
        String valuesNonTerminals = "ABCDEFGHIJKLMNOPQRTUVWXYZ";
        String startSymbol = "S";



        String[] choosenTerminals = new String[anzTerminals];
        String[] choosenNonTerminals = new String[anzNonTerminals];


        List<GrammarRule> choosenProductions = this.generateProductions(choosenTerminals, choosenNonTerminals);

        this.generateJsonFile(choosenTerminals, choosenNonTerminals, choosenProductions);

        return success;
    }

    private void generateJsonFile(String[] choosenTerminals, String[] choosenNonTerminals, List<GrammarRule> choosenProductions) {
    }

    private List<GrammarRule> generateProductions(String[] choosenTerminals, String[] choosenNonTerminals) {
        return null;
    }

    private void generateGrammarJson() {

    }

}
