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

	private final float PROBABILTY_FOR_NEW_NON_TERMINAL = 0.3f;
	private final float PROBABILTY_FOR_TERMINAL = 0.4f;
	private final float PROBABILTY_FOR_MULTIPLE_RIGHT_SIDE = 0.3f;

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
		float probabiltyForTerminal = PROBABILTY_FOR_TERMINAL;
		float probabiltyForNewNonTerminal = PROBABILTY_FOR_NEW_NON_TERMINAL;
		List<String> generatedProductions = new ArrayList<>();
		String generatedStartSymbol = nonTerminals.iterator().next();
		setStartSymbol(generatedStartSymbol);
		generatedProductions.add(generateStartProduction());

		while (!neededNonTerminalsOnLeftSide.isEmpty()) {
			float probabiltyForMultipleRightSide = PROBABILTY_FOR_MULTIPLE_RIGHT_SIDE;
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
		float probabilityForMultipleRightSide = PROBABILTY_FOR_MULTIPLE_RIGHT_SIDE;
		String rightSide = "";
		if (rand.nextFloat() <= 0.7) {
			while (StringUtils.isBlank(rightSide) || startSymbol.equals(rightSide)) {
				rightSide = nonTerminals.get(rand.nextInt(nonTerminals.size()));
				if (!startSymbol.equals(rightSide)) {
					addToNeededNonTerminalsOnLeftSide(rightSide);
				}
			}
		} else {
			rightSide = terminals.get(rand.nextInt(terminals.size()));
		}

		while (rand.nextFloat() <= probabilityForMultipleRightSide) {
			if (rand.nextFloat() <= 0.5) {
				String nonTerminal = nonTerminals.get(rand.nextInt(nonTerminals.size()));
				rightSide += nonTerminal;
				addToNeededNonTerminalsOnLeftSide(nonTerminal);
			} else {
				rightSide += terminals.get(rand.nextInt(terminals.size()));
			}
			probabilityForMultipleRightSide -= 0.1;
		}
		return startSymbol + "->" + rightSide;
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

	private boolean notNeedAnotherProductionForLeftSide(List<String> productions, char leftSide) {
		for (String currProd : productions) {
			if (currProd.charAt(0) == leftSide && !currProd.substring(1).contains(String.valueOf(currProd.charAt(0)))) {
				return true;
			}
		}
		return false;
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
		if (rightSide != null && rightSide.contains(leftSide) && notNeedAnotherProductionForLeftSide(generatedProductions, leftSide.charAt(0))) {
			neededNonTerminalsOnLeftSide.remove(leftSide);
		} else if (rightSide != null){
			if (rightSide.contains(leftSide) && !notNeedAnotherProductionForLeftSide(generatedProductions, leftSide.charAt(0))) {
				return leftSide;
			}
			for (char element : rightSide.toCharArray()) {
				if (Character.isUpperCase(element) && isLoop(generatedProductions, leftSide, element)) {
					return leftSide;
				}
			}
			neededNonTerminalsOnLeftSide.remove(leftSide);
		}
		return leftSide;
	}

	private boolean isLoop(List<String> productions, String leftSide, char rightSide) {
		List<String> productionsWithRightSideOnLeft = new ArrayList<>();
		productions.forEach(production -> {
			if (production.substring(production.indexOf('>') + 1 ).contains(leftSide)) {
				productionsWithRightSideOnLeft.add(production);
			}
		});

		for (String production : productionsWithRightSideOnLeft) {
			if (production.charAt(0) == rightSide) {
				return true;
			} else if (production.charAt(0) == startSymbol.charAt(0)) {
				return true;
			} else {
				return isLoop(productionsWithRightSideOnLeft, String.valueOf(production.charAt(0)), rightSide);
			}
		}
		return false;
	}

	private void addToNeededNonTerminalsOnLeftSide(String nonTerminal) {
		if (!neededNonTerminalsOnLeftSide.contains(nonTerminal)) {
			neededNonTerminalsOnLeftSide.add(nonTerminal);
		}
	}
}
