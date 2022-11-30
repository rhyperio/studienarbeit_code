package de.dhbw.karlsruhe.grammar.genertion;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import de.dhbw.karlsruhe.models.Grammar;

public class GrammarGeneration {

	private List<String> terminals = new ArrayList<>();
	private List<String> nonTerminals = new ArrayList<>();
	private List<String> productions = new ArrayList<>();
	private String startSymbol;

	public Grammar generateGrammar() {

		terminals = generateTerminals();
		nonTerminals = generateNonTerminals();
		productions = generateProductions();

		return new Grammar(terminals.toArray(new String[0]), nonTerminals.toArray(new String[0]),
				productions.toArray(new String[0]), startSymbol);
	}

	private List<String> generateTerminals() {
		List<String> terminals = new ArrayList<>();
		while (terminals.size() < 5) {
			terminals.add(RandomStringUtils.randomAlphabetic(1).toLowerCase());
		}
		return terminals;
	}

	private List<String> generateNonTerminals() {
		List<String> generatedNonTerminals = new ArrayList<>();
		while (generatedNonTerminals.size() < 5) {
			generatedNonTerminals.add(RandomStringUtils.randomAlphabetic(1).toUpperCase());
		}
		return generatedNonTerminals;
	}

	private List<String> generateProductions() {
		List<String> generatedProductions = new ArrayList<>();
		Random rand = new Random();

		String generatedStartSymbol = nonTerminals.iterator().next();
		setStartSymbol(generatedStartSymbol);

		List<String> neededNonTerminalsOnLeftSide = new ArrayList<>();
		// Added production for startsymbol
		String nonTerminal = nonTerminals.get(rand.nextInt(nonTerminals.size()));
		neededNonTerminalsOnLeftSide.add(nonTerminal);
		generatedProductions.add(startSymbol + "->" + nonTerminal);

		float probabiltyForTerminal = 0.2f;
		float probabiltyForNonTerminal = 0.3f;

		while (!neededNonTerminalsOnLeftSide.isEmpty()) {
			if (rand.nextFloat() <= probabiltyForTerminal) {
				String nonTerminal4 = "";
				if (rand.nextFloat() <= probabiltyForNonTerminal) {
					if (rand.nextFloat() <= 0.5) {
						nonTerminal4 = neededNonTerminalsOnLeftSide
								.get(rand.nextInt(neededNonTerminalsOnLeftSide.size()));
					} else {
						nonTerminal4 = nonTerminals.get(rand.nextInt(nonTerminals.size()));
						neededNonTerminalsOnLeftSide.add(nonTerminal4);
					}
					probabiltyForNonTerminal -= 0.1;
				}
				String terminal = terminals.get(rand.nextInt(terminals.size()));
				String nonTerminal2 = neededNonTerminalsOnLeftSide
						.get(rand.nextInt(neededNonTerminalsOnLeftSide.size()));
				neededNonTerminalsOnLeftSide.remove(nonTerminal2);
				generatedProductions.add(nonTerminal2 + "->" + terminal + " " + nonTerminal4);
			} else {
				probabiltyForTerminal += 0.1;
				String nonTerminal3 = nonTerminals.get(rand.nextInt(nonTerminals.size()));
				neededNonTerminalsOnLeftSide.add(nonTerminal3);
				generatedProductions.add(startSymbol + "->" + nonTerminal3);
			}
		}
		Set<String> set = new HashSet<>(generatedProductions);
		generatedProductions.clear();
		generatedProductions.addAll(set);

		return generatedProductions;
	}

	private boolean checkGeneratedProductions(List<String> generatedProductions) {
		return areAllNonTerminalsAreOnLeftSide(generatedProductions) && areAllTerminalsInUsage(generatedProductions);
	}

	private boolean areAllNonTerminalsAreOnLeftSide(List<String> generatedProductions) {
		for (String nonTerminal : nonTerminals) {
			if (generatedProductions.stream().anyMatch(production -> StringUtils.startsWith(production, nonTerminal))) {
				return true;
			}
		}
		return false;
	}

	private boolean areAllTerminalsInUsage(List<String> generatedProductions) {
		for (String terminal : terminals) {
			if (generatedProductions.stream()
					.anyMatch(production -> StringUtils.contains(production.substring(2), terminal))) {
				return true;
			}
		}
		return false;
	}

	private void setStartSymbol(String startSymbol) {
		this.startSymbol = startSymbol;
	}

}
