package de.dhbw.karlsruhe.models;

public class Grammar {

  private String[] terminals;
  private String[] nonTerminals;
  private String[] productions;
  private String startSymbol;

  public Grammar(String[] terminals, String[] nonTerminals, String[] productions, String startSymbol) {
	this.terminals = terminals;
	this.nonTerminals = nonTerminals;
	this.productions = productions;
	this.startSymbol = startSymbol;
}

  public String[] getTerminals() {
    return terminals;
  }

  public String[] getNonTerminals() {
    return nonTerminals;
  }

  public String[] getProductions() {
    return productions;
  }

  public String getStartSymbol() {
    return startSymbol;
  }

}
