package de.dhbw.karlsruhe.word.generation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.services.ProductionService;

import javax.swing.text.html.parser.Parser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class WordGeneration {

    private final Grammar grammar;
    private final ProductionService productionService = new ProductionService();
    private final Random rand = new Random();
    private final float preferEndProductionProbabilityLimit = 0.9f;
    private int currentProductionCount;
    private int maxProductionCount;
    private int maxReadCount;
    private int currentReadCount;
    private int maxActionCount;
    private int currentActionCount;

    public WordGeneration(Grammar grammar){
        this.grammar = grammar;
    }

    public String generateWord(ParserLimitation parserLimitation) throws WordLimitationsNotFulfillableException {
        return generateWord(parserLimitation,1);
    }

    public String generateWord(ParserLimitation parserLimitation, int minWordLength) throws WordLimitationsNotFulfillableException{

        this.maxReadCount = parserLimitation.getMaxReadCount();
        this.maxActionCount = parserLimitation.getMaxActionCount();
        List<GrammarProduction> startProductions = getPotentialStartProductions();

        int countTries = 0;
        String word;
        do {
            countTries++;
            if (countTries>100){
                throw new WordLimitationsNotFulfillableException();
            }

            currentReadCount = 0;
            currentActionCount = 0;

            GrammarProduction firstProduction = startProductions.get(rand.nextInt(startProductions.size()));
            try {
                word = traverseProductionsWithParserLimitations(firstProduction);
                word = word.replace("ε", "");
            } catch (ToManyProductionsException e){
                word="";
            }
        } while (word.length() < minWordLength);
        return word;
    }

    private String traverseProductionsWithParserLimitations(GrammarProduction production) throws ToManyProductionsException{

        if (productionService.isEndProduction(production)) {
            currentReadCount += production.rightSide().length();
            if (currentReadCount > maxReadCount)
                throw new ToManyProductionsException(maxReadCount,maxActionCount);
            return production.rightSide();
        }

        StringBuilder wordFragment = new StringBuilder();
        for (int i =0; i<production.rightSide().length();i++) {
            char currentSymbol = production.rightSide().charAt(i);
            if (Arrays.stream(grammar.getTerminals()).toList().contains(String.valueOf(currentSymbol))){
                currentReadCount++;
                if (currentReadCount > maxReadCount)
                    throw new ToManyProductionsException(maxReadCount,maxActionCount);
                wordFragment.append(currentSymbol);
            }
            else {
                List<GrammarProduction> potentialProduction = getPotentialProductions(String.valueOf(currentSymbol));
                currentActionCount++;
                if (currentActionCount > maxActionCount)
                    throw new ToManyProductionsException(maxReadCount,maxActionCount);
                wordFragment.append(traverseProductionsWithParserLimitations(potentialProduction.get(rand.nextInt(potentialProduction.size()))));
            }
        }
        return wordFragment.toString();
    }

    public String generateWord(int maxProductionCount) throws WordLimitationsNotFulfillableException{
        return generateWord(1,1000, maxProductionCount);
    }

    public String generateWord() throws WordLimitationsNotFulfillableException{
        return generateWord(1,10, 100);
    }

    public String generateWord( int minWordLength, int maxWordLength, int maxProductionCount) throws WordLimitationsNotFulfillableException{
        this.maxProductionCount = maxProductionCount;
        List<GrammarProduction> startProductions = getPotentialStartProductions();

        int countTries = 0;
        String word;
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
            for (int i =0; i<production.rightSide().length();i++) {
                char currentSymbol = production.rightSide().charAt(i);
                if (Arrays.stream(grammar.getTerminals()).toList().contains(String.valueOf(currentSymbol))){
                    wordFragment.append(currentSymbol);
                }
                else {
                    List<GrammarProduction> potentialProduction = getPotentialProductions(String.valueOf(currentSymbol));
                    if (currentProductionCount > preferEndProductionProbabilityLimit * maxProductionCount){
                        List<GrammarProduction> potentialEndProductions = getEndProductions(potentialProduction);
                        if (!potentialEndProductions.isEmpty()) {
                            if (++currentProductionCount > maxProductionCount)
                                throw new ToManyProductionsException(maxProductionCount);
                            wordFragment.append(potentialEndProductions.get(rand.nextInt(potentialEndProductions.size())).rightSide());
                        }
                        else
                            wordFragment.append(traverseProductions(potentialProduction.get(rand.nextInt(potentialProduction.size()))));
                    } else {
                        wordFragment.append(traverseProductions(potentialProduction.get(rand.nextInt(potentialProduction.size()))));
                    }
                }
            }
            return wordFragment.toString();
        }
    }

    private List<GrammarProduction> getPotentialProductions(String nonTerminal) {
        List<GrammarProduction> potentialProduction = new ArrayList<>();
        for (GrammarProduction p : grammar.getProductions()){
            if (p.leftSide().equals(nonTerminal))
                potentialProduction.add(p);
        }
        return potentialProduction;
    }

    private List<GrammarProduction> getEndProductions(List<GrammarProduction> potentialProductions){
        List<GrammarProduction> endProductions = new ArrayList<>();
        for (GrammarProduction production : potentialProductions){
            if (productionService.isEndProduction(production)){
                endProductions.add(production);
            }
        }
        return endProductions;
    }
}
