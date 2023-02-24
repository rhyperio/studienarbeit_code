package de.dhbw.karlsruhe.grammar.generation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.dhbw.karlsruhe.models.GrammarRule;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import de.dhbw.karlsruhe.models.Grammar;

import static org.junit.jupiter.api.Assertions.*;

class GrammarGenerationTest {

	@Test
	 void checkEmptyIntersectionBetweenTerminalsAndNonTerminals() {
		GrammarGeneration grammarGeneration = new GrammarGeneration();
		Grammar generatedGrammar = grammarGeneration.generateGrammar();

		assertTrue(Arrays.stream(generatedGrammar.getTerminals()).distinct()
				.filter(x -> Arrays.asList(generatedGrammar.getNonTerminals()).contains(x)).toList()
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

	@Test
	void checkAllNonTerminalsOnLeftSide(){
		GrammarGeneration grammarGeneration = new GrammarGeneration();
		Grammar generatedGrammar = grammarGeneration.generateGrammar();

		for (String nonTerminal : generatedGrammar.getNonTerminals()) {
			List<GrammarRule> grList = new ArrayList<>((Arrays.stream(generatedGrammar.getProductionsAsGrammarRule()).toList()));
			if (grList.stream().noneMatch(production -> StringUtils.startsWith(production.leftSide(), nonTerminal))) {
				fail();
			}
		}
		assertTrue(true);
	}

	@Test
	void checkAllTerminalsUsed(){
		GrammarGeneration grammarGeneration = new GrammarGeneration();
		Grammar generatedGrammar = grammarGeneration.generateGrammar();

		for (String terminal : generatedGrammar.getTerminals()) {
			List<GrammarRule> grList = new ArrayList<>((Arrays.stream(generatedGrammar.getProductionsAsGrammarRule()).toList()));
			if (grList.stream().noneMatch(production -> StringUtils.contains(production.rightSide(), terminal) && !production.rightSide().equals("epsilon"))) {
				fail();
			}
		}
		assertTrue(true);
	}

	@Test
	void checkAtLeastOneEndproduction() {
		for (int i =0; i<1000; i++) {
			GrammarGeneration grammarGeneration = new GrammarGeneration();
			Grammar generatedGrammar = grammarGeneration.generateGrammar();
			boolean endProductionExists = false;
			for (GrammarRule gr : generatedGrammar.getProductionsAsGrammarRule()) {
				if (gr.isEndProduction()) {
					endProductionExists = true;
				}
			}
			if (!endProductionExists){
				fail();
			}
		}
		assertTrue(true);
	}

}
