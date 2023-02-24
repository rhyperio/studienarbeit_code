package de.dhbw.karlsruhe.grammar.genertion;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import de.dhbw.karlsruhe.models.Grammar;

public class GrammarProbabilityGeneration extends GrammarGeneration {

	/*
	 * Probabilities which are used during grammar production generation
	 */
	private final float PROBABILITY_FOR_NEW_NON_TERMINAL = 0.5f;
	private final float PROBABILITY_FOR_TERMINAL = 0.4f;
	private final float PROBABILITY_FOR_MULTIPLE_RIGHT_SIDE = 0.6f;
	private final float PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_ON_RIGHT_SIDE = 0.5f;
	private final float PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_IN_START_PRODUCTION = 0.8f;
	private final float DECREASING_PROBABILITY_FACTOR = 0.1f;
	private final Random rand = new Random();
	private final List<String> neededNonTerminalsOnLeftSide = new ArrayList<>();

	public Grammar generateGrammar() {
		terminals = generateTerminals();
		nonTerminals = generateNonTerminals();
		List<String> productions = generateProductions();
		return new Grammar(terminals.toArray(new String[0]), nonTerminals.toArray(new String[0]),
				productions.toArray(new String[0]), startSymbol);
	}

	protected List<String> generateProductions() {
		float probabilityForTerminal = PROBABILITY_FOR_TERMINAL;
		float probabilityForNewNonTerminal = PROBABILITY_FOR_NEW_NON_TERMINAL;
		List<String> generatedProductions = new ArrayList<>();
		String generatedStartSymbol = nonTerminals.iterator().next();
		setStartSymbol(generatedStartSymbol);
		generatedProductions.add(generateStartProduction());

		while (!neededNonTerminalsOnLeftSide.isEmpty()) {
			float probabilityForMultipleRightSide = PROBABILITY_FOR_MULTIPLE_RIGHT_SIDE;
			if (rand.nextFloat() <= probabilityForTerminal) {
				StringBuilder rightSide = new StringBuilder(terminals.get(rand.nextInt(terminals.size())));
				while (rand.nextFloat() <= probabilityForMultipleRightSide) {
					expandRightSide(rightSide, probabilityForNewNonTerminal);
					probabilityForNewNonTerminal = probabilityForNewNonTerminal - DECREASING_PROBABILITY_FACTOR;
					probabilityForMultipleRightSide = probabilityForMultipleRightSide - DECREASING_PROBABILITY_FACTOR;
				}
				generatedProductions.add(buildProduction(getNonTerminalForLeftSide(generatedProductions, rightSide.toString()), rightSide.toString()));
			} else {
				generateNonTerminalProduction(generatedProductions);
				probabilityForTerminal += DECREASING_PROBABILITY_FACTOR;
			}
		}
		return cleanProductions(generatedProductions);
	}

	private void setStartSymbol(String startSymbol) {
		this.startSymbol = startSymbol;
	}

	private String generateStartProduction() {
		float probabilityForMultipleRightSide = PROBABILITY_FOR_MULTIPLE_RIGHT_SIDE;
		StringBuilder rightSide = new StringBuilder();
		if (rand.nextFloat() <= PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_IN_START_PRODUCTION) {
			while (StringUtils.isBlank(rightSide.toString()) || startSymbol.equals(rightSide.toString())) {
				rightSide = new StringBuilder(nonTerminals.get(rand.nextInt(nonTerminals.size())));
				if (!startSymbol.equals(rightSide.toString())) {
					addToNeededNonTerminalsOnLeftSide(rightSide.toString());
				}
			}
		} else {
			rightSide = new StringBuilder(terminals.get(rand.nextInt(terminals.size())));
		}

		while (rand.nextFloat() <= probabilityForMultipleRightSide) {
			if (rand.nextFloat() <= PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_ON_RIGHT_SIDE) {
				String nonTerminal = nonTerminals.get(rand.nextInt(nonTerminals.size()));
				rightSide.append(nonTerminal);
				addToNeededNonTerminalsOnLeftSide(nonTerminal);
			} else {
				rightSide.append(terminals.get(rand.nextInt(terminals.size())));
			}
			probabilityForMultipleRightSide -= DECREASING_PROBABILITY_FACTOR;
		}
		return buildProduction(startSymbol, rightSide.toString());
	}

	private void expandRightSide(StringBuilder rightSide, float probabilityForNewNonTerminal) {
		if (rand.nextFloat() <= PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_ON_RIGHT_SIDE) {
			extendRightSideWithSeveralNonTerminals(rightSide, probabilityForNewNonTerminal);
		} else {
			// Füge noch ein Terminal zur rechten Seite hinzu
			rightSide.append(terminals.get(rand.nextInt(terminals.size())));
		}
	}

	private void extendRightSideWithSeveralNonTerminals(StringBuilder rightSide, float probabilityForNewNonTerminal) {
		// Füge noch ein Nichtterminal zur rechten Seite hinzu
		if (rand.nextFloat() <= probabilityForNewNonTerminal) {
			expandRightSideWithNewNonTerminal(rightSide);
		} else {
			expandRightSideWithNonTerminal(rightSide);
		}
	}

	private void generateNonTerminalProduction(List<String> generatedProductions) {
		String nonTerminalLeft = getNonTerminalForLeftSide(generatedProductions, null);
		String nonTerminalRight = nonTerminals.get(rand.nextInt(nonTerminals.size()));

		while (nonTerminalRight.equals(nonTerminalLeft)) {
			nonTerminalRight = nonTerminals.get(rand.nextInt(nonTerminals.size()));
		}

		addToNeededNonTerminalsOnLeftSide(nonTerminalRight);
		generatedProductions.add(buildProduction(nonTerminalLeft, nonTerminalRight));
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

	private boolean notNeedAnotherProductionForLeftSide(List<String> productions, char leftSide) {
		for (String currProd : productions) {
			if (currProd.charAt(0) == leftSide && !currProd.substring(1).contains(String.valueOf(currProd.charAt(0)))) {
				return true;
			}
		}
		return false;
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
				if (Character.isUpperCase(element) && isLoop(startSymbol, generatedProductions, leftSide, element)) {
					return leftSide;
				}
			}
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
