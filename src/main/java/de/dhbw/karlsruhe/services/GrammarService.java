package de.dhbw.karlsruhe.services;

import com.google.gson.Gson;
import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarRule;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GrammarService {

  private final Grammar grammar;

  public GrammarService(String json) {
    grammar = formatGrammar(json);
  }

  public List<GrammarRule> getGrammarRules() {
    return createGrammarRules(grammar);
  }

  public String getStartSymbol() {
    return grammar.getStartSymbol();
  }

  public String[] getTerminals() {
    return this.grammar.getTerminals();
  }

  public String[] getNonTerminals() {
    return this.grammar.getNonTerminals();
  }

  public boolean checkStringOnlyContainsGrammarTerminals(String word){
    Stream<String> wordSplit = Arrays.stream(word.split("(?!^)"));
    String[] terminals = this.getSortedGrammarTerminals();
    for (String terminal: terminals) {
      wordSplit = wordSplit.filter(s -> !s.equals(terminal));
    }
    return wordSplit.toList().isEmpty();
  }

  private Grammar formatGrammar(String json) {
    Gson gson = new Gson();
    return gson.fromJson(json, Grammar.class);
  }

  private List<GrammarRule> createGrammarRules(Grammar grammar) {
    List<GrammarRule> grammarRules = new ArrayList<>();
    for (String production : grammar.getProductions()) {
      grammarRules.addAll(buildGrammarRules(production));
    }
    return grammarRules;
  }

  private List<GrammarRule> buildGrammarRules(String production) {
    List<GrammarRule> grammarRules = new ArrayList<>();
    String[] splitRule = production.split("->");
    String leftSide = splitRule[0];
    String completeRightSide = splitRule[1];
    String[] rightSides = completeRightSide.split("\\|");

    for (String rightSide : rightSides) {
      grammarRules.add(new GrammarRule(leftSide, rightSide.trim()));
    }
    return grammarRules;
  }

}
