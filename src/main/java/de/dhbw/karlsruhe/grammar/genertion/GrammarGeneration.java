package de.dhbw.karlsruhe.grammar.genertion;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

import de.dhbw.karlsruhe.models.Grammar;

public class GrammarGeneration {
	
	public Grammar generateGrammar() {
		String[] terminals = {};
		String[] nonTerminals = {};
		String[] productions = {};
		String startSymbol = "";
		
		return new Grammar(terminals, nonTerminals, productions, startSymbol);
	}
	
	public Set<String> generateTerminals() {
		Set<String> terminals = new HashSet<String>();
		while (terminals.size() < 5) {
			terminals.add(RandomStringUtils.randomAlphabetic(1).toLowerCase());
		}
		return terminals;
	}
	
	public Set<String> generateNonTerminals() {
		Set<String> nonTerminals = new HashSet<String>();
		while (nonTerminals.size() < 5) {
			nonTerminals.add(RandomStringUtils.randomAlphabetic(1).toUpperCase());
		}
		return nonTerminals;
	}
	
	public Set<String> generateProductions() {
		return Set.of();
	}
	
	public String generateStartSymbol() {
		return "S";
	}

}
