package de.dhbw.karlsruhe.grammar.generation;

import java.util.*;

import de.dhbw.karlsruhe.models.GrammarRule;
import de.dhbw.karlsruhe.models.ProductionRightSide;
import de.dhbw.karlsruhe.models.ProductionSet;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import de.dhbw.karlsruhe.models.Grammar;

public class GrammarGeneration {

	private List<String> terminals = new ArrayList<>();
	private List<String> nonTerminals = new ArrayList<>();
	private List<String> productions = new ArrayList<>();
	private String startSymbol;
	private Random rand = new Random();

	public Grammar generateGrammar() {

		terminals = generateTerminals();
		nonTerminals = generateNonTerminals();

		for (GrammarRule gr: generateProductions()){
			productions.add(gr.toString());
		}

		return new Grammar(terminals.toArray(new String[0]), nonTerminals.toArray(new String[0]),
				productions.toArray(new String[0]), startSymbol);
	}

	private List<String> generateTerminals() {
		List<String> tmpTerminals = new ArrayList<>();
		while (tmpTerminals.size() < 4) {
			tmpTerminals.add(RandomStringUtils.randomAlphabetic(1).toLowerCase());
		}
		return tmpTerminals;
	}

	private List<String> generateNonTerminals() {
		List<String> generatedNonTerminals = new ArrayList<>();
		while (generatedNonTerminals.size() < 4) {
			String newNonTerminal = RandomStringUtils.randomAlphabetic(1).toUpperCase();
			if (!generatedNonTerminals.contains(newNonTerminal))
				generatedNonTerminals.add(newNonTerminal);
		}
		return generatedNonTerminals;
	}

	private List<GrammarRule> generateProductions() {
		List<GrammarRule> generatedProductions = new ArrayList<>();

		String generatedStartSymbol = nonTerminals.iterator().next();
		setStartSymbol(generatedStartSymbol);

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
			generatedProductions.add(new GrammarRule(nonTerminal, rightSide));
		}
		generatedProductions = generatedProductions.stream().distinct().toList();

		generatedProductions = completeProductions(generatedProductions);

		System.out.println(startSymbol);
		System.out.println(terminals);
		System.out.println(nonTerminals);
		System.out.println(generatedProductions);

		return generatedProductions;

	}

	private List<GrammarRule> completeProductions(List<GrammarRule> generatedProductions) {

		ProductionSet pSet = new ProductionSet(generatedProductions.get(0));
		List<GrammarRule> grammarRules = new ArrayList<>(generatedProductions);

		int pSetsize = 0;
		int tmpSize = -1;
		while (pSetsize != tmpSize){
			tmpSize = pSet.size();
			for (GrammarRule gr : generatedProductions) {
				pSet.addProduction(gr);
			}
			pSetsize = pSet.size();
		}

		do{
			for (int i = 0; i< grammarRules.size();i++) {
				if (pSet.isRuleInSet(grammarRules.get(i)))
					continue;
				if (pSet.isOnRightSide(grammarRules.get(i).leftSide())){
					pSet.addProduction(grammarRules.get(i));
				}else {
					GrammarRule tmpGR = generateSingleProduction(pSet.getRandomRightSideNonTerminal(), grammarRules.get(i).leftSide());
					grammarRules.add(tmpGR);
					pSet.addProduction(tmpGR);
				}
			}
			grammarRules = new ArrayList<>(new HashSet<>(grammarRules));
		} while (pSet.size() != grammarRules.size());

		return grammarRules;

	}

	private GrammarRule generateSingleProduction(String leftSideNonTerminal, String rightSideNonTerminal){
		ProductionRightSide production = ProductionRightSide.randomProduction();
		String[] rightSideCompounds = production.rightSide.split(" ");
		for (int i = 0; i<rightSideCompounds.length; i++) {
			if (StringUtils.equals(rightSideCompounds[i], "t")){
				int index = rand.nextInt(terminals.size());
				rightSideCompounds[i] = terminals.get(index);
			}

			if (StringUtils.equals(rightSideCompounds[i], "N")){
					rightSideCompounds[i] = rightSideNonTerminal.toUpperCase();
			}
		}

		String rightSide = String.join(" ",rightSideCompounds);

		return new GrammarRule(leftSideNonTerminal, rightSide);
	}

	private boolean checkGeneratedProductions(List<String> generatedProductions) {
		return areAllNonTerminalsOnLeftSide(generatedProductions) && areAllTerminalsInUsage(generatedProductions);
	}

	private boolean areAllNonTerminalsOnLeftSide(List<String> generatedProductions) {
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
