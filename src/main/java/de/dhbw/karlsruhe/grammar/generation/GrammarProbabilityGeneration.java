package de.dhbw.karlsruhe.grammar.generation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import de.dhbw.karlsruhe.models.GrammarProduction;
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
	private List<String> usableTerminals;
	private List<String> usableNonTerminals;

	public Grammar generateGrammar() {
		usableTerminals = generateTerminals();
		usableNonTerminals = generateNonTerminals();
		List<GrammarProduction> productions = generateProductions();
		return new Grammar(terminals.toArray(new String[0]), nonTerminals.toArray(new String[0]),
				productions.toArray(new GrammarProduction[0]), startSymbol);
	}

	protected List<GrammarProduction> generateProductions() {
		float probabilityForTerminal = PROBABILITY_FOR_TERMINAL;
		float probabilityForNewNonTerminal = PROBABILITY_FOR_NEW_NON_TERMINAL;
		List<GrammarProduction> generatedProductions = new ArrayList<>();
		String generatedStartSymbol = usableNonTerminals.iterator().next();
		setStartSymbol(generatedStartSymbol);
		generatedProductions.add(generateStartProduction());

		while (!neededNonTerminalsOnLeftSide.isEmpty()) {
			float probabilityForMultipleRightSide = PROBABILITY_FOR_MULTIPLE_RIGHT_SIDE;
			if (rand.nextFloat() <= probabilityForTerminal) {
				StringBuilder rightSide = new StringBuilder(usableTerminals.get(rand.nextInt(usableTerminals.size())));
				addToTerminals(rightSide.toString());
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
		addNonTerminal(startSymbol);
		this.startSymbol = startSymbol;
	}

	private GrammarProduction generateStartProduction() {
		float probabilityForMultipleRightSide = PROBABILITY_FOR_MULTIPLE_RIGHT_SIDE;
		StringBuilder rightSide = new StringBuilder();
		if (rand.nextFloat() <= PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_IN_START_PRODUCTION) {
			while (StringUtils.isBlank(rightSide.toString()) || startSymbol.equals(rightSide.toString())) {
				String nonTerminal = usableNonTerminals.get(rand.nextInt(usableNonTerminals.size()));
				rightSide = new StringBuilder(nonTerminal);
				if (!startSymbol.equals(rightSide.toString())) {
					addToNeededNonTerminalsOnLeftSide(rightSide.toString());
				}
			}
		} else {
			String terminal = usableTerminals.get(rand.nextInt(usableTerminals.size()));
			addToTerminals(terminal);
			rightSide = new StringBuilder(terminal);
		}

		while (rand.nextFloat() <= probabilityForMultipleRightSide) {
			if (rand.nextFloat() <= PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_ON_RIGHT_SIDE) {
				String nonTerminal = usableNonTerminals.get(rand.nextInt(usableNonTerminals.size()));
				rightSide.append(nonTerminal);
				addToNeededNonTerminalsOnLeftSide(nonTerminal);
			} else {
				String terminal = usableTerminals.get(rand.nextInt(usableTerminals.size()));
				rightSide.append(terminal);
				addToTerminals(terminal);
			}
			probabilityForMultipleRightSide -= DECREASING_PROBABILITY_FACTOR;
		}
		return buildProduction(startSymbol, rightSide.toString());
	}

	private void expandRightSide(StringBuilder rightSide, float probabilityForNewNonTerminal) {
		if (rand.nextFloat() <= PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_ON_RIGHT_SIDE) {
			extendRightSideWithNonTerminal(rightSide, probabilityForNewNonTerminal);
		} else {
			// Füge noch ein Terminal zur rechten Seite hinzu
			String terminal = usableTerminals.get(rand.nextInt(usableTerminals.size()));
			addToTerminals(terminal);
			rightSide.append(terminal);
		}
	}

	private void extendRightSideWithNonTerminal(StringBuilder rightSide, float probabilityForNewNonTerminal) {
		// Füge noch ein Nichtterminal zur rechten Seite hinzu
		if (rand.nextFloat() <= probabilityForNewNonTerminal) {
			expandRightSideWithNewNonTerminal(rightSide);
		} else {
			expandRightSideWithNonTerminal(rightSide);
		}
	}

	private void generateNonTerminalProduction(List<GrammarProduction> generatedProductions) {
		String nonTerminalLeft = getNonTerminalForLeftSide(generatedProductions, null);
		String nonTerminalRight = usableNonTerminals.get(rand.nextInt(usableNonTerminals.size()));

		while (nonTerminalRight.equals(nonTerminalLeft)) {
			nonTerminalRight = usableNonTerminals.get(rand.nextInt(usableNonTerminals.size()));
		}

		addToNeededNonTerminalsOnLeftSide(nonTerminalRight);
		generatedProductions.add(buildProduction(nonTerminalLeft, nonTerminalRight));
	}

	private void expandRightSideWithNonTerminal(StringBuilder rightSide) {
		String newNonTerminal =
				neededNonTerminalsOnLeftSide.get(rand.nextInt(neededNonTerminalsOnLeftSide.size()));
		rightSide.append(newNonTerminal);
	}

	private void expandRightSideWithNewNonTerminal(StringBuilder rightSide) {
		String newNonTerminal = usableNonTerminals.get(rand.nextInt(usableNonTerminals.size()));
		if (neededNonTerminalsOnLeftSide.contains(newNonTerminal)) {
			rightSide.append(newNonTerminal);
		} else {
			rightSide.append(newNonTerminal);
			addToNeededNonTerminalsOnLeftSide(newNonTerminal);
		}
	}

	private boolean notNeedAnotherProductionForLeftSide(List<GrammarProduction> productions, char leftSide) {
		for (GrammarProduction currProd : productions) {
			if (currProd.leftSide().charAt(0) == leftSide &&
					!currProd.rightSide().contains(String.valueOf(currProd.leftSide().charAt(0)))) {
				return true;
			}
		}
		return false;
	}

	private String getNonTerminalForLeftSide(List<GrammarProduction> generatedProductions, String rightSide) {
		String leftSide = neededNonTerminalsOnLeftSide.get(rand.nextInt(neededNonTerminalsOnLeftSide.size()));
		if (rightSide != null &&
				rightSide.contains(leftSide) &&
				notNeedAnotherProductionForLeftSide(generatedProductions, leftSide.charAt(0))) {
			for (char element : rightSide.toCharArray()) {
				if (Character.isUpperCase(element) && isLoop(startSymbol, generatedProductions, leftSide, element)) {
					addNonTerminal(leftSide);
					return leftSide;
				}
			}
			neededNonTerminalsOnLeftSide.remove(leftSide);
		} else if (rightSide != null) {
			if (rightSide.contains(leftSide) && !notNeedAnotherProductionForLeftSide(generatedProductions, leftSide.charAt(0))) {
				addNonTerminal(leftSide);
				return leftSide;
			}
			for (char element : rightSide.toCharArray()) {
				if (Character.isUpperCase(element) && isLoop(startSymbol, generatedProductions, leftSide, element)) {
					addNonTerminal(leftSide);
					return leftSide;
				}
			}
			neededNonTerminalsOnLeftSide.remove(leftSide);
		}
		addNonTerminal(leftSide);
		return leftSide;
	}

	private void addToNeededNonTerminalsOnLeftSide(String nonTerminal) {
		if (!neededNonTerminalsOnLeftSide.contains(nonTerminal)) {
			neededNonTerminalsOnLeftSide.add(nonTerminal);
		}
	}

	private void addToTerminals(String terminal) {
		if (!terminals.contains(terminal)) {
			terminals.add(terminal);
		}
	}

	private void addNonTerminal(String nonTerminal) {
		if (!nonTerminals.contains(nonTerminal)) {
			nonTerminals.add(nonTerminal);
		}
	}
}
