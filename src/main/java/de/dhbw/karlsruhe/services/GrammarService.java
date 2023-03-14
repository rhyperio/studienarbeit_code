package de.dhbw.karlsruhe.services;

import com.google.gson.Gson;
import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class GrammarService {

  private final Grammar grammar;

  public GrammarService(String json) {
    grammar = formatGrammar(json);
    grammar.splitOrGrammarsIntoSingleRules();
  }

  public GrammarService(Grammar grammar) {
    this.grammar = grammar;
  }

    public List<GrammarProduction> getGrammarRules () {
      return Arrays.stream(grammar.getProductions()).toList();
    }

    public String getStartSymbol () {
      return grammar.getStartSymbol();
    }

    public String[] getTerminals () {
      return this.grammar.getTerminals();
    }

    public String[] getNonTerminals () {
      return this.grammar.getNonTerminals();
    }

    public boolean checkStringOnlyContainsGrammarTerminals (String word){
      Stream<String> wordSplit = Arrays.stream(word.split("(?!^)"));
      String[] terminals = this.getSortedGrammarTerminals();
      for (String terminal : terminals) {
        wordSplit = wordSplit.filter(s -> !s.equals(terminal));
      }
      return wordSplit.toList().isEmpty();
    }

    private String[] getSortedGrammarTerminals () {
      String[] tmp = grammar.getTerminals();
      Arrays.sort(tmp);
      return tmp;
    }

    private Grammar formatGrammar (String json){
      Gson gson = new Gson();
      return gson.fromJson(json, Grammar.class);
    }

}
