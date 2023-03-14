package de.dhbw.karlsruhe.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


  public void splitOrGrammarsIntoSingleRules() {
    List<GrammarProduction> grammarRules = new ArrayList<>();

    Arrays.stream(this.productions).forEach(production -> {
      String[] rightSides = production.rightSide().split("\\|");

      for (String rightSide : rightSides) {
        grammarRules.add(new GrammarProduction(production.leftSide(), removeSpacesIn(rightSide.trim())));
      }
    });
    this.productions = grammarRules.toArray(new GrammarProduction[0]);
  }

  public void mergeOrGrammarsIntoSingleRules() {
    List<GrammarProduction> cleanedProductions = new ArrayList<>();

    for (GrammarProduction currProduction : this.productions) {
      char first = currProduction.leftSide().charAt(0);
      if (isNotAlreadyInList(cleanedProductions, first)) {
        StringBuilder buildCurrRightSide = new StringBuilder(currProduction.rightSide());
        for (GrammarProduction production: this.productions) {
          if (currProduction.equals(production)) {
            continue;
          }
          char second = production.leftSide().charAt(0);
          if (first == second) {
            buildCurrRightSide.append(" | ").append(production.rightSide());
          }
        }
        cleanedProductions.add(new GrammarProduction(currProduction.leftSide(), buildCurrRightSide.toString()));
      }
    }
    this.productions = cleanedProductions.toArray(new GrammarProduction[0]);
  }

  private boolean isNotAlreadyInList(List<GrammarProduction> productions, char leftSide) {
    for (GrammarProduction currProd: productions) {
      if (currProd.leftSide().charAt(0) == leftSide) {
        return false;
      }
    }
    return true;
  }

  private String removeSpacesIn(String value) {
      return value.replaceAll(" ", "");
  }

  @Override
  public String toString() {
    // ToDo: Adapt to String Method
    return super.toString();
  }
}
