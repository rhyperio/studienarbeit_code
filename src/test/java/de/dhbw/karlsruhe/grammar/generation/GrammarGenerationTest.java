package de.dhbw.karlsruhe.grammar.generation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import de.dhbw.karlsruhe.grammar.genertion.GrammarGeneration;
import de.dhbw.karlsruhe.models.Grammar;

public class GrammarGenerationTest {

	@Test
	public void checkEmptyInersectionBetweenTerminalsAndNonTerminals() {
		GrammarGeneration grammarGeneration = new GrammarGeneration();
		Grammar generatedGrammar = grammarGeneration.generateGrammar();

		assertTrue(Arrays.stream(generatedGrammar.getTerminals()).distinct()
				.filter(x -> Arrays.stream(generatedGrammar.getNonTerminals()).anyMatch(y -> y == x)).toList()
				.isEmpty());
	}

	@Test
	public void checkThatGrammarParstAreNotEmpty() {
		GrammarGeneration grammarGeneration = new GrammarGeneration();
		Grammar generatedGrammar = grammarGeneration.generateGrammar();

		assertNotEquals(0, generatedGrammar.getTerminals().length);
		assertNotEquals(0, generatedGrammar.getNonTerminals().length);
		assertNotEquals(0, generatedGrammar.getProductions().length);
		assertFalse(generatedGrammar.getStartSymbol().isBlank());
	}

	@Test
	public void checkThatStartSymbolIsNonTerminal() {
		GrammarGeneration grammarGeneration = new GrammarGeneration();
		Grammar generatedGrammar = grammarGeneration.generateGrammar();

		assertTrue(
				Arrays.stream(generatedGrammar.getNonTerminals()).anyMatch(generatedGrammar.getStartSymbol()::equals));
	}
}
