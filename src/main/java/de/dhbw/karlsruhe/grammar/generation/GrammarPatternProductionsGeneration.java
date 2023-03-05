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
		startSymbol = nonTerminals.iterator().next();
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
		List<GrammarProduction> generatedProductions = generateProductionsForEachNonTerminal();

		generatedProductions = connectProductions(generatedProductions);

		generatedProductions = completeTerminalProductions(generatedProductions);

		generatedProductions = completeEndProductions(generatedProductions);

		return generatedProductions;
	}

	private List<GrammarProduction> generateProductionsForEachNonTerminal() {
		List<GrammarProduction> generatedProductions = new ArrayList<>();
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
							rightSideCompounds[i] = nonTerminals.get((index+1) % nonTerminals.size());
					}
			}
			String rightSide = String.join(" ",rightSideCompounds);
			generatedProductions.add(new GrammarProduction(nonTerminal, rightSide));
		}
		return generatedProductions.stream().distinct().toList();
	}

	private List<GrammarProduction> connectProductions(List<GrammarProduction> generatedProductions) {
		List<GrammarProduction> startProductions = new ArrayList<>(generatedProductions.stream().filter(pr -> pr.leftSide().contains(startSymbol)).toList());
		ProductionSet pSet = new ProductionSet(startProductions);
		pSet.addAllReachableProductions(generatedProductions);

		// Generate new productions that connect the productions in the productionSet with the remaining ones until all are connected/reachable
		List<GrammarProduction> grammarProductions = new ArrayList<>(generatedProductions);
		while (pSet.size() != grammarProductions.size()) {
			for (int i = 0; i< grammarProductions.size();i++) {
				if (pSet.isRuleInSet(grammarProductions.get(i)))
					continue;

				if (!pSet.addProduction(grammarProductions.get(i))){
					GrammarProduction tmpProduction = generateSingleProduction(pSet.getRandomRightSideNonTerminal(), grammarProductions.get(i).leftSide());
					grammarProductions.add(tmpProduction);
					pSet.addProduction(tmpProduction);
				}
			}
			grammarProductions = new ArrayList<>(new HashSet<>(grammarProductions));
		}
		return grammarProductions;
	}

	private List<GrammarProduction> completeTerminalProductions(List<GrammarProduction> grammarProductions){
		List<GrammarProduction> resultProductions = new ArrayList<>(grammarProductions);
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

	private List<GrammarProduction> completeEndProductions(List<GrammarProduction> grammarRules) {
		List<GrammarProduction> endProductions = new ArrayList<>(grammarRules.stream().filter(GrammarProduction::isEndProduction).toList());
		List<GrammarProduction> allProductions = new ArrayList<>(grammarRules);

		if (endProductions.isEmpty()) {
			GrammarProduction tmpProduction = getEndProduction(grammarRules.get(rand.nextInt(grammarRules.size())).leftSide());
			endProductions.add(tmpProduction);
			allProductions.add(tmpProduction);
		}

		int completeSize = -1;

		while (completeSize != allProductions.size()) {
			completeSize = allProductions.size();
			ProductionSet fromEndProductionsReachableProductions = new ProductionSet(endProductions);
			List<GrammarProduction> notFromEndProductionsReachableProductions = new ArrayList<>(allProductions);
			notFromEndProductionsReachableProductions.removeAll(endProductions);

			int tmpSize;
			do {
				tmpSize = fromEndProductionsReachableProductions.size();
				for (GrammarProduction remainingPR : allProductions) {
					if (fromEndProductionsReachableProductions.addProductionInReverse(remainingPR)) {
						notFromEndProductionsReachableProductions.remove(remainingPR);
					}
				}
			} while (tmpSize != fromEndProductionsReachableProductions.size());


			if (!notFromEndProductionsReachableProductions.isEmpty()) {
				List<String> remainingNonTerminals =
						new ArrayList<>(Objects.requireNonNull(notFromEndProductionsReachableProductions.stream().findAny().get().getRightSideNonTerminal()));
				GrammarProduction newProduction = getEndProduction(
						remainingNonTerminals.get(rand.nextInt(remainingNonTerminals.size())));
				allProductions.add(newProduction);
				endProductions.add(newProduction);
			}
		}
		return allProductions;
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
				if (Arrays.stream(rightSideCompounds).noneMatch(s -> s.contains(rightSideNonTerminal.toUpperCase()))) {
					rightSideCompounds[i] = rightSideNonTerminal.toUpperCase();
				} else {
					int index = rand.nextInt(nonTerminals.size());
					rightSideCompounds[i] = nonTerminals.get(index);
				}
			}
		}
		String rightSide = String.join(" ",rightSideCompounds);

		return new GrammarProduction(leftSideNonTerminal, rightSide);
	}

}
