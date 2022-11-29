package de.dhbw.karlsruhe.grammar.genertion;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;

import de.dhbw.karlsruhe.models.Grammar;

public class GrammarGeneration {
	
	private String[] terminals = {};
	private String[] nonTerminals = {};
	private String[] productions = {};
	private String startSymbol;
	
	public Grammar generateGrammar() {
		
		return new Grammar(terminals, nonTerminals, productions, startSymbol);
	}
	
	public Set<String> generateTerminals() {
		Set<String> terminals = new HashSet<>();
		while (terminals.size() < 5) {
			terminals.add(RandomStringUtils.randomAlphabetic(1).toLowerCase());
		}
		return terminals;
	}
	
	public Set<String> generateNonTerminals() {
		Set<String> generatedNonTerminals = new HashSet<String>();
		while (generatedNonTerminals.size() < 5) {
			generatedNonTerminals.add(RandomStringUtils.randomAlphabetic(1).toUpperCase());
		}
		return generatedNonTerminals;
	}
	
	public Set<String> generateProductions(Set<String> terminals, Set<String> nonTerminals) {
		Set<String> generatedProductions = new HashSet<>();
	    String generatedStartSymbol = nonTerminals.iterator().next();
	    setStartSymbol(generatedStartSymbol);
	    
	    boolean finishedGeneration = false;
	    
	    while (!finishedGeneration) {
	    	generatedProductions.add(generateProduction());
	    	finishedGeneration = checkGeneratedProductions(generatedProductions);
		}
	    
		return generatedProductions;
	}
	
	private boolean checkGeneratedProductions(Set<String> generatedProductions) {
		return generatedProductions.isEmpty();
	}

	private void setStartSymbol(String startSymbol) {
		this.startSymbol = startSymbol;
	}
	
	private String generateProduction() {
		return "";
	}

}
