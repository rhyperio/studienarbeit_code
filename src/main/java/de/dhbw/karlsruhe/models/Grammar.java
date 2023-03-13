package de.dhbw.karlsruhe.models;

public class Grammar {

  private String[] terminals;
  private String[] nonTerminals;
  private GrammarProduction[] productions;
  private String startSymbol;

  // Default constructor for JSON deserialization
  public Grammar() {
  }

  public Grammar(String[] terminals, String[] nonTerminals, GrammarProduction[] productions, String startSymbol) {
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

  public GrammarProduction[] getProductions(){
    return productions;
  }

  public String getStartSymbol() {
    return startSymbol;
  }

}
