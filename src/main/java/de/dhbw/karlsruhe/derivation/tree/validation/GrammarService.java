package de.dhbw.karlsruhe.derivation.tree.validation;

import com.google.gson.Gson;
import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarRule;
import java.util.ArrayList;
import java.util.List;

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
