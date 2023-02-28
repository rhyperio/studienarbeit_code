package de.dhbw.karlsruhe.grammar.generation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import de.dhbw.karlsruhe.models.Grammar;

public class GrammarProbabilityGenerationTest {

	@Test
	public void checkEmptyIntersectionBetweenTerminalsAndNonTerminals() {
		GrammarGeneration grammarProbabilityGeneration = new GrammarProbabilityGeneration();
		Grammar generatedGrammar = grammarProbabilityGeneration.generateGrammar();

		assertTrue(Arrays.stream(generatedGrammar.getTerminals()).distinct()
				.filter(x -> Arrays.asList(generatedGrammar.getNonTerminals()).contains(x)).toList()
				.isEmpty());
	}

	@Test
	public void checkThatGrammarGenerationsAreNotEmpty() {
		GrammarGeneration grammarProbabilityGeneration = new GrammarProbabilityGeneration();
		Grammar generatedGrammar = grammarProbabilityGeneration.generateGrammar();

		assertNotEquals(0, generatedGrammar.getTerminals().length);
		assertNotEquals(0, generatedGrammar.getNonTerminals().length);
		assertNotEquals(0, generatedGrammar.getProductions().length);
		assertFalse(generatedGrammar.getStartSymbol().isBlank());
	}

	@Test
	public void checkThatStartSymbolIsNonTerminal() {
		GrammarGeneration grammarProbabilityGeneration = new GrammarProbabilityGeneration();
		Grammar generatedGrammar = grammarProbabilityGeneration.generateGrammar();

		assertTrue(
				Arrays.asList(generatedGrammar.getNonTerminals()).contains(generatedGrammar.getStartSymbol()));
	}
}
