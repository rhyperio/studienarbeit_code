package de.dhbw.karlsruhe.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

  public String[] getProductionsAsString() {
    return Arrays.stream(productions).map(GrammarProduction::toString).toArray(String[]::new);
  }

  public GrammarProduction[] getProductionsAsGrammarProductions(){
    return productions;
  }

  public String getStartSymbol() {
    return startSymbol;
  }

}
