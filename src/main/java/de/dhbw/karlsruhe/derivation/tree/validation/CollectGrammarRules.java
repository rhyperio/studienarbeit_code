package de.dhbw.karlsruhe.derivation.tree.validation;

import com.google.gson.Gson;
import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarRule;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class CollectGrammarRules {

  public List<GrammarRule> getGrammarRules() {
    try {
      Grammar grammar = formatGrammar();
      return createGrammarRules(grammar);
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }
    return List.of();
  }

  private Grammar formatGrammar() throws FileNotFoundException {
    Gson gson = new Gson();
    Reader reader = new FileReader("src/main/resources/test_examples/exampleGrammar.json");
    return gson.fromJson(reader, Grammar.class);
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
