package de.dhbw.karlsruhe.word.generation;

import de.dhbw.karlsruhe.grammar.generation.GrammarConcatenationGeneration;
import de.dhbw.karlsruhe.grammar.generation.GrammarGeneration;
import de.dhbw.karlsruhe.grammar.generation.GrammarPatternProductionsGeneration;
import de.dhbw.karlsruhe.grammar.generation.GrammarProbabilityGeneration;
import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.models.Probability;
import de.dhbw.karlsruhe.services.GrammarService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class WordGenerationTest {

    @Test
    void isWordGeneratedTest() {
        for (int i=0; i<100;i++) {
            GrammarGeneration grammarGeneration = getGrammarGeneration();
            Grammar grammar = grammarGeneration.generateGrammar();
            WordGeneration wordGeneration = new WordGeneration(grammar);
            String word;
            try {
                word = wordGeneration.generateWord();
            }catch (WordLimitationsNotFulfillableException e){
                continue;
            }
            assertNotNull(word);
            assertNotEquals("", word);
        }
    }

    @Test
    void generatedWordContainsGrammarTerminalsTest() {
        for (int i=0; i<100;i++) {
            GrammarGeneration grammarGeneration = getGrammarGeneration();
            Grammar grammar = grammarGeneration.generateGrammar();
            GrammarService grammarService = new GrammarService(grammar);
            WordGeneration wordGeneration = new WordGeneration(grammar);
            String word;
            try {
                word = wordGeneration.generateWord();
            }catch (WordLimitationsNotFulfillableException e){
                continue;
            }
            assertTrue(grammarService.checkStringOnlyContainsGrammarTerminals(word));
        }
    }

    @Test
    void generatedWordNotContainsEpsilonTest() {
        for (int i=0; i<100;i++) {
            GrammarGeneration grammarGeneration = getGrammarGeneration();
            Grammar grammar = grammarGeneration.generateGrammar();
            WordGeneration wordGeneration = new WordGeneration(grammar);
            String word;
            try {
                word = wordGeneration.generateWord();
            }catch (WordLimitationsNotFulfillableException e){
                continue;
            }
            assertFalse(word.contains("ε"));
        }
    }

    @Test
    void maxProductionCountTest() {
        for (int i = 0; i < 100; i++) {
            GrammarGeneration grammarGeneration = getGrammarGeneration();
            Grammar grammar = grammarGeneration.generateGrammar();
            WordGeneration wordGeneration = new WordGeneration(grammar);
            String word;
            try {
                word = wordGeneration.generateWord(1);
                List<GrammarProduction> potentialProduction = getPotentialProductions(grammar.getStartSymbol(), grammar);
                assertTrue(potentialProduction.stream().anyMatch(p -> p.rightSide().equals(word)));
            } catch (WordLimitationsNotFulfillableException e) {
                // Skip not fitting grammars
            }
        }
    }

    @Test
    void ReadCountTest() {
        for (int i = 0; i < 100; i++) {
            GrammarGeneration grammarGeneration = getGrammarGeneration();
            Grammar grammar = grammarGeneration.generateGrammar();
            WordGeneration wordGeneration = new WordGeneration(grammar);
            String word;
            try {
                word = wordGeneration.generateWordWithParserLimitations(3,10);
                assertTrue(word.length() <= 3);
            } catch (WordLimitationsNotFulfillableException e) {
                // Skip not fitting grammars
            }
        }
    }

    private List<GrammarProduction> getPotentialProductions(String nonTerminal, Grammar grammar) {
        List<GrammarProduction> potentialProduction = new ArrayList<>();
        for (GrammarProduction p : grammar.getProductions()){
            if (p.leftSide().contains(nonTerminal))
                potentialProduction.add(p);
        }
        return potentialProduction;
    }

    private GrammarGeneration getGrammarGeneration(){
        Random rand = new Random();

        return switch (rand.nextInt(3)) {
            case 0 -> new GrammarProbabilityGeneration(new Probability(
                    0.5f,
                    0.4f,
                    0.6f,
                    0.5f,
                    0.8f,
                    0.1f));
            case 1 -> new GrammarConcatenationGeneration();
            case 2 -> new GrammarPatternProductionsGeneration();
            default -> null;
        };
    }
}
