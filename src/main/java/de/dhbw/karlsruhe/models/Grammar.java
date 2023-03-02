package de.dhbw.karlsruhe.models;

import java.util.ArrayList;
import java.util.List;

public class Grammar {

  private String[] terminals;
  private String[] nonTerminals;
  private String[] productions;
  private String startSymbol;

  public Grammar(String[] pTerminals, String[] pNonTerminals, List<GrammarRule> pGrammarRules, String pStartSymbol) {
    this.terminals = pTerminals;
    this.nonTerminals = pNonTerminals;
    this.productions = this.parseGrammarRulesToStringArray(pGrammarRules);
    this.startSymbol = pStartSymbol;
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

  private String[] parseGrammarRulesToStringArray(List<GrammarRule> pGrammarRules) {
    List<String> parsedProductions = new ArrayList<>();
    String tempProduction;

    for (GrammarRule gr : pGrammarRules) {
      tempProduction = gr.leftSide() + "->" + gr.rightSide();
      parsedProductions.add(tempProduction);
    }

    return parsedProductions.toArray(new String[0]);
  }

  @Override
  public String toString() {
    // ToDo: Adapt to String Method
    return super.toString();
  }
}
