package de.dhbw.karlsruhe.derivation.tree.validation;

import com.google.gson.Gson;
import de.dhbw.karlsruhe.derivation.tree.models.Grammar;
import de.dhbw.karlsruhe.derivation.tree.models.GrammarRule;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    return Arrays.stream(grammar.getProductions()).toList().stream().map(this::buildGrammarRule).collect(Collectors.toList());
  }

  private GrammarRule buildGrammarRule(String production) {
    String[] splitRule = production.split("->");
    String leftSide = splitRule[0];
    String rightSide = splitRule[1];
    return new GrammarRule(leftSide, rightSide);
  }

}
