package de.dhbw.karlsruhe.word.generation;

import de.dhbw.karlsruhe.grammar.generation.GrammarGeneration;
import de.dhbw.karlsruhe.grammar.generation.GrammarPatternProductionsGeneration;
import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.services.GrammarService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordGenerationTest {

    @Test
    void isWordGeneratedTest() {
        for (int i=0; i<100;i++) {
            GrammarGeneration grammarGeneration = new GrammarPatternProductionsGeneration();
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
            GrammarGeneration grammarGeneration = new GrammarPatternProductionsGeneration();
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
            GrammarGeneration grammarGeneration = new GrammarPatternProductionsGeneration();
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
}
