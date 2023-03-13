package de.dhbw.karlsruhe.grammar.generation;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

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
		assertNotEquals(0, generatedGrammar.getProductionsAsGrammarProductions().length);
		assertFalse(generatedGrammar.getStartSymbol().isBlank());
	}

	@Test
	public void checkThatStartSymbolIsNonTerminal() {
		GrammarGeneration grammarProbabilityGeneration = new GrammarProbabilityGeneration();
		Grammar generatedGrammar = grammarProbabilityGeneration.generateGrammar();

		assertTrue(
				Arrays.asList(generatedGrammar.getNonTerminals()).contains(generatedGrammar.getStartSymbol()));
	}

	@Test
	void checkAllNonTerminalsOnLeftSide(){
		GrammarGeneration grammarGeneration = new GrammarProbabilityGeneration();
		Grammar generatedGrammar = grammarGeneration.generateGrammar();

		List<GrammarProduction> grList = new ArrayList<>(Arrays.stream(generatedGrammar.getProductionsAsGrammarProductions()).toList());
		for (String nonTerminal : generatedGrammar.getNonTerminals()) {
			assertTrue(grList.stream().anyMatch(production -> StringUtils.startsWith(production.leftSide(), nonTerminal)));
		}
	}

	@Test
	void checkAllTerminalsUsed(){
		GrammarGeneration grammarGeneration = new GrammarProbabilityGeneration();
		Grammar generatedGrammar = grammarGeneration.generateGrammar();

		List<GrammarProduction> grList = new ArrayList<>(Arrays.stream(generatedGrammar.getProductionsAsGrammarProductions()).toList());
		for (String terminal : generatedGrammar.getTerminals()) {
			assertTrue(grList.stream().anyMatch(production -> StringUtils.contains(production.rightSide(), terminal) || production.rightSide().equals("epsilon")));
		}
	}

	@Test
	void checkAtLeastOneEndproduction() {
		for (int i =0; i<1000; i++) {
			GrammarGeneration grammarGeneration = new GrammarProbabilityGeneration();
			Grammar generatedGrammar = grammarGeneration.generateGrammar();
			List<GrammarProduction> grList = new ArrayList<>(Arrays.stream(generatedGrammar.getProductionsAsGrammarProductions()).toList());
			assertTrue(grList.stream().anyMatch(GrammarProduction::isEndProduction));
		}
	}

}
