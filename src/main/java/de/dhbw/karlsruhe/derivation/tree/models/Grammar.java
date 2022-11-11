package de.dhbw.karlsruhe.derivation.tree.models;

public class Grammar {

  private String[] terminals;
  private String[] nonTerminals;
  private String[] productions;
  private String startSymbol;

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
