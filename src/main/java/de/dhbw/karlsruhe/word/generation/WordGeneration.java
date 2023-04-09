package de.dhbw.karlsruhe.word.generation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.services.ProductionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WordGeneration {

    private final Grammar grammar;
    private final ProductionService productionService = new ProductionService();
    private final Random rand = new Random();
    private int currentProductionCount;

    private int maxProductionCount;

    public WordGeneration(Grammar grammar){
        this.grammar = grammar;
    }

    public String generateWord(int maxProductionCount) throws WordLimitationsNotFulfillableException{
        return generateWord(10000,1,maxProductionCount);
    }

    public String generateWord() throws WordLimitationsNotFulfillableException{
        return generateWord(10,1, 100);
    }

    public String generateWord(int maxWordLength, int minWordLength, int maxProductionCount) throws WordLimitationsNotFulfillableException{
        this.maxProductionCount = maxProductionCount;
        List<GrammarProduction> startProductions = getPotentialStartProductions();

        int countTries = 0;
        String word = "";
        do {
            countTries++;
            if (countTries>100){
                throw new WordLimitationsNotFulfillableException();
            }
            currentProductionCount = 0;
            GrammarProduction firstProduction = startProductions.get(rand.nextInt(startProductions.size()));
            try {
                word = traverseProductions(firstProduction);
                word = word.replace("ε", "");
            } catch (ToManyProductionsException e){
                word="";
            }
        } while (maxWordLength< word.length() || word.length() < minWordLength);
        return word;
    }

    private List<GrammarProduction> getPotentialStartProductions() {
        String startNonTerminal = grammar.getStartSymbol();
        List<GrammarProduction> startProductions = new ArrayList<>();
        for (GrammarProduction production : grammar.getProductions()){
            if (production.leftSide().contains(startNonTerminal))
                startProductions.add(production);
        }
        return startProductions;
    }

    private String traverseProductions(GrammarProduction production) throws ToManyProductionsException{
        currentProductionCount++;
        if (currentProductionCount > maxProductionCount)
            throw new ToManyProductionsException(maxProductionCount);
        if (productionService.isEndProduction(production)) {
            return production.rightSide();
        }
        else {
            StringBuilder wordFragment = new StringBuilder();
            for (int i =0; i<production.rightSide().length();i++){
                char currentSymbol = production.rightSide().charAt(i);
                if (Arrays.stream(grammar.getTerminals()).toList().contains(String.valueOf(currentSymbol))){
                    wordFragment.append(currentSymbol);
                }
                else {
                    List<GrammarProduction> potentialProduction = getPotentialProductions(String.valueOf(currentSymbol));
                    wordFragment.append(traverseProductions(potentialProduction.get(rand.nextInt(potentialProduction.size()))));
                }
            }
            return wordFragment.toString();
        }
    }

    private List<GrammarProduction> getPotentialProductions(String nonTerminal) {
        List<GrammarProduction> potentialProduction = new ArrayList<>();
        for (GrammarProduction p : grammar.getProductions()){
            if (p.leftSide().contains(nonTerminal))
                potentialProduction.add(p);
        }
        return potentialProduction;
    }
}
