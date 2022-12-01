package de.dhbw.karlsruhe.grammar.generation;

import java.util.*;

import de.dhbw.karlsruhe.models.ProductionRightSide;
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
			String newNonTerminal = RandomStringUtils.randomAlphabetic(1).toUpperCase();
			if (!generatedNonTerminals.contains(newNonTerminal))
				generatedNonTerminals.add(newNonTerminal);
		}
		return generatedNonTerminals;
	}

	private List<String> generateProductions() {
		List<String> generatedProductions = new ArrayList<>();
		Random rand = new Random();

		String generatedStartSymbol = nonTerminals.iterator().next();
		setStartSymbol(generatedStartSymbol);

		String randNonTerminal = nonTerminals.get(rand.nextInt(nonTerminals.size()));

		while (StringUtils.equals(randNonTerminal, startSymbol))
			randNonTerminal = nonTerminals.get(rand.nextInt(nonTerminals.size()));

		generatedProductions.add(startSymbol + "->" + randNonTerminal);

		for ( String nonTerminal: nonTerminals) {
			ProductionRightSide production = ProductionRightSide.randomProduction();
			String[] rightSideCompounds = production.rightSide.split(" ");
			for (int i = 0; i<rightSideCompounds.length; i++) {
					if (StringUtils.equals(rightSideCompounds[i], "t")){
						int index = rand.nextInt(terminals.size());
						rightSideCompounds[i] = terminals.get(index);

					}
					if (StringUtils.equals(rightSideCompounds[i], "N")){
						int index = rand.nextInt(nonTerminals.size());
						if (production == ProductionRightSide.p2 &&
								!rightSideCompounds[i].equals(terminals.get(index))) {
							rightSideCompounds[i] = nonTerminals.get(index);
						}else
							rightSideCompounds[i] = nonTerminals.get((index+1) % terminals.size());
					}
			}

			String rightSide = String.join(" ",rightSideCompounds);
			generatedProductions.add(nonTerminal + "->" + rightSide);
		}
		generatedProductions = generatedProductions.stream().distinct().toList();

		System.out.println(startSymbol);
		System.out.println(terminals);
		System.out.println(nonTerminals);
		System.out.println(generatedProductions);


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
