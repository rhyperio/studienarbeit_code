package de.dhbw.karlsruhe.grammar.generation;

import java.util.*;

import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.models.ProductionRightSide;
import de.dhbw.karlsruhe.models.ProductionSet;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import de.dhbw.karlsruhe.models.Grammar;

public class GrammarPatternProductionsGeneration extends GrammarGeneration{

	private List<String> terminals = new ArrayList<>();
	private List<String> nonTerminals = new ArrayList<>();
	private List<GrammarProduction> productions = new ArrayList<>();
	private String startSymbol;
	private Random rand = new Random();

	public Grammar generateGrammar() {
		return generateGrammar(3,3);
	}

	public Grammar generateGrammar(int countTerminals, int countNonTerminals) {

		terminals = generateTerminals(countTerminals);
		nonTerminals = generateNonTerminals(countNonTerminals);
		productions = generateProductions();

		return new Grammar(terminals.toArray(new String[0]), nonTerminals.toArray(new String[0]),
				productions.toArray(new GrammarProduction[0]), startSymbol);
	}

	private List<String> generateTerminals(int countTerminals) {
		List<String> tmpTerminals = new ArrayList<>();
		while (tmpTerminals.size() < countTerminals) {
			String tmpStr = RandomStringUtils.randomAlphabetic(1).toLowerCase();
			if (!tmpTerminals.contains(tmpStr)) {
				tmpTerminals.add(tmpStr);
			}
		}
		return tmpTerminals;
	}

	private List<String> generateNonTerminals(int countNonTerminals) {
		List<String> generatedNonTerminals = new ArrayList<>();
		while (generatedNonTerminals.size() < countNonTerminals) {
			String newNonTerminal = RandomStringUtils.randomAlphabetic(1).toUpperCase();
			if (!generatedNonTerminals.contains(newNonTerminal))
				generatedNonTerminals.add(newNonTerminal);
		}
		return generatedNonTerminals;
	}

	protected List<GrammarProduction> generateProductions() {
		List<GrammarProduction> generatedProductions = new ArrayList<>();

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
								!nonTerminal.equals(nonTerminals.get(index))) {
							rightSideCompounds[i] = nonTerminals.get(index);
						}else
							rightSideCompounds[i] = nonTerminals.get((index+1) % terminals.size());
					}
			}

			String rightSide = String.join(" ",rightSideCompounds);
			generatedProductions.add(new GrammarProduction(nonTerminal, rightSide));
		}
		generatedProductions = generatedProductions.stream().distinct().toList();

		return completeProductions(generatedProductions);
	}

	private List<GrammarProduction> completeProductions(List<GrammarProduction> generatedProductions) {

		ProductionSet pSet = new ProductionSet(generatedProductions.get(0));
		List<GrammarProduction> grammarRules = new ArrayList<>(generatedProductions);

		int pSetsize = 0;
		int tmpSize = -1;
		while (pSetsize != tmpSize){
			tmpSize = pSet.size();
			for (GrammarProduction gr : generatedProductions) {
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
					GrammarProduction tmpGR = generateSingleProduction(pSet.getRandomRightSideNonTerminal(), grammarRules.get(i).leftSide());
					grammarRules.add(tmpGR);
					pSet.addProduction(tmpGR);
				}
			}
			grammarRules = new ArrayList<>(new HashSet<>(grammarRules));
		} while (pSet.size() != grammarRules.size());

		return completeEndProductions(completeTerminalProductions(grammarRules));
	}

	private List<GrammarProduction> completeEndProductions(List<GrammarProduction> grammarRules) {
		List<GrammarProduction> endProductions = new ArrayList<>(grammarRules.stream().filter(GrammarProduction::isEndProduction).toList());
		List<GrammarProduction> completeProductions = new ArrayList<>(grammarRules);

		if (endProductions.isEmpty()) {
			GrammarProduction tmpProduction = getEndProduction(grammarRules.get(rand.nextInt(grammarRules.size())).leftSide());
			endProductions.add(tmpProduction);
			completeProductions.add(tmpProduction);
		}

		int completeSize = -1;

		while (completeSize != completeProductions.size()) {
			completeSize = completeProductions.size();
			ProductionSet tmpProductions = new ProductionSet(endProductions);
			List<GrammarProduction> remainingProductions = new ArrayList<>(completeProductions);
			remainingProductions.removeAll(endProductions);

			for (GrammarProduction pr : endProductions) {
				tmpProductions.addProduction(pr);
				int tmpSize;
				do {
					tmpSize = tmpProductions.size();
					for (GrammarProduction remainingPR : completeProductions) {
						int shortTmpSize = tmpProductions.size();
						tmpProductions.addProductionInReverse(remainingPR);
						if (shortTmpSize != tmpProductions.size()) {
							remainingProductions.remove(remainingPR);
						}
					}
				} while (tmpSize != tmpProductions.size());
			}

			if (!remainingProductions.isEmpty()) {
				List<String> nonTerminals = new ArrayList<>(Objects.requireNonNull(remainingProductions.stream().findAny().get().getRightSideNonTerminal()));
				GrammarProduction newProduction = getEndProduction(
						nonTerminals.get(rand.nextInt(nonTerminals.size())));
				completeProductions.add(newProduction);
				endProductions.add(newProduction);
			}
		}
		return completeProductions;
	}

	private GrammarProduction getEndProduction(String leftSide) {
		ProductionRightSide productionRightSide = ProductionRightSide.randomEndProduction();
		String[] rightSideCompounds = productionRightSide.rightSide.split(" ");
		for (int i = 0; i<rightSideCompounds.length; i++) {
			if (StringUtils.equals(rightSideCompounds[i], "t")){
				int index = rand.nextInt(terminals.size());
				rightSideCompounds[i] = terminals.get(index);
			}
		}
		String rightSide = String.join(" ",rightSideCompounds);

		return new GrammarProduction(leftSide, rightSide);
	}

	private GrammarProduction generateSingleProduction(String leftSideNonTerminal, String rightSideNonTerminal){
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

		return new GrammarProduction(leftSideNonTerminal, rightSide);
	}

	private List<GrammarProduction> completeTerminalProductions(List<GrammarProduction> grammarRules){
		List<GrammarProduction> resultProductions = new ArrayList<>(grammarRules);
		List<String> tmpTerminals = new ArrayList<>(terminals);
		for (String str: terminals) {
			for (GrammarProduction gr : resultProductions) {
				if (!gr.rightSide().contains("epsilon") && gr.rightSide().contains(str)){
					tmpTerminals.remove(str);
				}
			}
		}
		for (String terminal: tmpTerminals){
			int index = rand.nextInt(terminals.size());
			resultProductions.add(new GrammarProduction(nonTerminals.get(index), terminal));
		}

		return resultProductions;
	}

	private void setStartSymbol(String startSymbol) {
		this.startSymbol = startSymbol;
	}

}
