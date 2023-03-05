package de.dhbw.karlsruhe.models;

public class Grammar {

  private String[] terminals;
  private String[] nonTerminals;
  private GrammarProduction[] productions;
  private String startSymbol;

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

  public String[] getProductions() {
    String[] strArray = new String[productions.length];
    for (int i=0; i <strArray.length; i++){
      strArray[i] = productions[i].toString();
    }
    return strArray;
  }

  public GrammarProduction[] getProductionsAsGrammarProductions(){
    return productions;
  }

  public String getStartSymbol() {
    return startSymbol;
  }

}
