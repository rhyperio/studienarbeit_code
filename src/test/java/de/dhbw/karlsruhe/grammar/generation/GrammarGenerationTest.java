package de.dhbw.karlsruhe.grammar.generation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import de.dhbw.karlsruhe.grammar.genertion.GrammarGeneration;
import de.dhbw.karlsruhe.models.Grammar;

class GrammarGenerationTest {
	
	@Test
	void checkEmptyInersectionBetweenTerminalsAndNonTerminals() {
		GrammarGeneration grammarGeneration = new GrammarGeneration();
		Grammar generatedGrammar = grammarGeneration.generateGrammar();
		
		assertTrue(Arrays.stream(generatedGrammar.getTerminals())
		        .distinct()
		        .filter(x -> Arrays.stream(generatedGrammar.getNonTerminals()).anyMatch(y -> y == x))
		        .toList().isEmpty());
	}
	
	@Test
	void checkThatGrammarParstAreNotEmpty() {
		GrammarGeneration grammarGeneration = new GrammarGeneration();
		Grammar generatedGrammar = grammarGeneration.generateGrammar();
		
		assertNotEquals(generatedGrammar.getTerminals().length, 0);
		assertNotEquals(generatedGrammar.getNonTerminals().length, 0);
		assertNotEquals(generatedGrammar.getProductions().length, 0);
		assertFalse(generatedGrammar.getStartSymbol().isBlank());
	}
	
	@Test
	void checkThatStartSymbolIsNonTerminal() {
		GrammarGeneration grammarGeneration = new GrammarGeneration();
		Grammar generatedGrammar = grammarGeneration.generateGrammar();
		
		assertTrue(Arrays.stream(generatedGrammar.getNonTerminals())
				.anyMatch(generatedGrammar.getTerminals()::equals));
	}
}
