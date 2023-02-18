package de.dhbw.karlsruhe.grammar.generation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import de.dhbw.karlsruhe.models.Grammar;

class GrammarGenerationTest {

	@Test
	 void checkEmptyInersectionBetweenTerminalsAndNonTerminals() {
		GrammarGeneration grammarGeneration = new GrammarGeneration();
		Grammar generatedGrammar = grammarGeneration.generateGrammar();

		assertTrue(Arrays.stream(generatedGrammar.getTerminals()).distinct()
				.filter(x -> Arrays.stream(generatedGrammar.getNonTerminals()).anyMatch(y -> Objects.equals(y, x))).toList()
				.isEmpty());
	}

	@Test
	 void checkThatGrammarParstAreNotEmpty() {
		GrammarGeneration grammarGeneration = new GrammarGeneration();
		Grammar generatedGrammar = grammarGeneration.generateGrammar();

		assertNotEquals(0, generatedGrammar.getTerminals().length);
		assertNotEquals(0, generatedGrammar.getNonTerminals().length);
		assertNotEquals(0, generatedGrammar.getProductions().length);
		assertFalse(generatedGrammar.getStartSymbol().isBlank());
	}

	@Test
	 void checkThatStartSymbolIsNonTerminal() {
		GrammarGeneration grammarGeneration = new GrammarGeneration();
		Grammar generatedGrammar = grammarGeneration.generateGrammar();

		assertTrue(
				Arrays.asList(generatedGrammar.getNonTerminals()).contains(generatedGrammar.getStartSymbol()));
	}
}
