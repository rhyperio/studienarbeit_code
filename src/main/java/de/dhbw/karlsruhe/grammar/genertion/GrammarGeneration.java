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

	Random rand = new Random();
	private List<String> terminals = new ArrayList<>();
	private List<String> nonTerminals = new ArrayList<>();
	private List<String> productions = new ArrayList<>();
	private String startSymbol;
	private List<String> neededNonTerminalsOnLeftSide = new ArrayList<>();

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
			String terminal = RandomStringUtils.randomAlphabetic(1).toLowerCase();
			if (!terminals.contains(terminal)) {
				terminals.add(terminal);
			}
		}
		return terminals;
	}

	private List<String> generateNonTerminals() {
		List<String> generatedNonTerminals = new ArrayList<>();
		while (generatedNonTerminals.size() < 5) {
			String nonTerminal = RandomStringUtils.randomAlphabetic(1).toUpperCase();
			if (!generatedNonTerminals.contains(nonTerminal)) {
				generatedNonTerminals.add(nonTerminal);
			}
		}
		return generatedNonTerminals;
	}

	private List<String> generateProductions() {
		float probabiltyForMultipleRightSide = 0.8f;
		float probabiltyForTerminal = 0.8f;
		float probabiltyForNewNonTerminal = 0.3f;
		List<String> generatedProductions = new ArrayList<>();
		String generatedStartSymbol = nonTerminals.iterator().next();
		setStartSymbol(generatedStartSymbol);
		generatedProductions.add(generateStartProduction());

		while (!neededNonTerminalsOnLeftSide.isEmpty()) {
			probabiltyForMultipleRightSide = 0.8f;
			if (rand.nextFloat() <= probabiltyForTerminal) {
				StringBuilder rightSide = new StringBuilder(terminals.get(rand.nextInt(terminals.size())));
				while (rand.nextFloat() <= probabiltyForMultipleRightSide) {
					expandRightSide(rightSide, probabiltyForNewNonTerminal);
					probabiltyForNewNonTerminal = (float) (probabiltyForNewNonTerminal - 0.1);
					probabiltyForMultipleRightSide = (float) (probabiltyForMultipleRightSide - 0.1);
				}
				generatedProductions.add(getNonTerminalForLeftSide(generatedProductions, rightSide.toString()) + "->" + rightSide);
			} else {
				generateNonTerminalProduction(generatedProductions);
				probabiltyForTerminal += 0.1;
			}
		}
		return cleanUpProductions(generatedProductions);
	}

	private void expandRightSide(StringBuilder rightSide, float probabiltyForNewNonTerminal) {
		if (rand.nextFloat() <= 0.5) {
			extandRightSideWithSeveralNonTerminals(rightSide, probabiltyForNewNonTerminal);
		} else {
			// Füge noch ein Terminal zur rechten Seite hinzu
			rightSide.append(terminals.get(rand.nextInt(terminals.size())));
		}
	}

	private void extandRightSideWithSeveralNonTerminals(StringBuilder rightSide, float probabiltyForNewNonTerminal) {
		// Füge noch ein Nichtterminal zur rechten Seite hinzu
		if (rand.nextFloat() <= probabiltyForNewNonTerminal) {
			expandRightSideWithNewNonTerminal(rightSide);
		} else {
			expandRightSideWithNonTerminal(rightSide);
		}
	}

	private List<String> cleanUpProductions(List<String> generatedProductions) {
		Set<String> set = new HashSet<>(generatedProductions);
		generatedProductions.clear();
		generatedProductions.addAll(set);
		return cleanedProductions(generatedProductions);
	}

	private void generateNonTerminalProduction(List<String> generatedProductions) {
		String nonTerminalLeft = getNonTerminalForLeftSide(generatedProductions, null);
		String nonTerminalRight = nonTerminals.get(rand.nextInt(nonTerminals.size()));

		while (nonTerminalRight.equals(nonTerminalLeft)) {
			nonTerminalRight = nonTerminals.get(rand.nextInt(nonTerminals.size()));
		}

		addToNeededNonTerminalsOnLeftSide(nonTerminalRight);
		generatedProductions.add(nonTerminalLeft + "->" + nonTerminalRight);
	}

	private void expandRightSideWithNonTerminal(StringBuilder rightSide) {
		String newNonTerminal = neededNonTerminalsOnLeftSide.get(rand.nextInt(neededNonTerminalsOnLeftSide.size()));
		rightSide.append(newNonTerminal);
	}

	private void expandRightSideWithNewNonTerminal(StringBuilder rightSide) {
		String newNonTerminal = nonTerminals.get(rand.nextInt(nonTerminals.size()));
		if (neededNonTerminalsOnLeftSide.contains(newNonTerminal)) {
			rightSide.append(newNonTerminal);
		} else {
			rightSide.append(newNonTerminal);
			addToNeededNonTerminalsOnLeftSide(newNonTerminal);
		}
	}

	private String generateStartProduction() {
		String nonTerminal = nonTerminals.get(rand.nextInt(nonTerminals.size()));
		while (startSymbol.equals(nonTerminal)) {
			nonTerminal = nonTerminals.get(rand.nextInt(nonTerminals.size()));
		}
		addToNeededNonTerminalsOnLeftSide(nonTerminal);
		return startSymbol + "->" + nonTerminal;
	}

	private void setStartSymbol(String startSymbol) {
		this.startSymbol = startSymbol;
	}

	private List<String> cleanedProductions(List<String> generatedProductions) {
		List<String> cleanedProductions = new ArrayList<>();

		for (String currProduction : generatedProductions) {
			char first = currProduction.charAt(0);
			if (isNotAlreadyInList(cleanedProductions, first)) {
				StringBuilder currProductionBuilder = new StringBuilder(currProduction);
				for (String production2: generatedProductions) {
					if (currProduction.equals(production2)) {
						continue;
					}
					char second = production2.charAt(0);
					if (first == second) {
						currProductionBuilder.append(" | ").append(production2.substring(production2.indexOf('>') + 1));
					}
				}
				cleanedProductions.add(currProductionBuilder.toString());
			}
		}
		return cleanedProductions;
	}

	private boolean isNotAlreadyInList(List<String> productions, char leftSide) {
		for (String currProd: productions) {
			if (currProd.charAt(0) == leftSide) {
				return false;
			}
		}
		return true;
	}

	private String getNonTerminalForLeftSide(List<String> generatedProductions, String rightSide) {
		String leftSide = neededNonTerminalsOnLeftSide.get(rand.nextInt(neededNonTerminalsOnLeftSide.size()));
		if (rightSide != null && rightSide.contains(leftSide)) {
			if (!isNotAlreadyInList(generatedProductions, leftSide.charAt(0))) {
				neededNonTerminalsOnLeftSide.remove(leftSide);
			}
		} else {
			neededNonTerminalsOnLeftSide.remove(leftSide);
		}
		return leftSide;
	}

	private void addToNeededNonTerminalsOnLeftSide(String nonTerminal) {
		if (!neededNonTerminalsOnLeftSide.contains(nonTerminal)) {
			neededNonTerminalsOnLeftSide.add(nonTerminal);
		}
	}
}
