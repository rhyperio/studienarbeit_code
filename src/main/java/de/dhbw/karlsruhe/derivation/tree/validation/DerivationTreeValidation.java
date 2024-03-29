package de.dhbw.karlsruhe.derivation.tree.validation;

import de.dhbw.karlsruhe.derivation.tree.models.DerivationTree;
import de.dhbw.karlsruhe.derivation.tree.models.DetailResult;
import de.dhbw.karlsruhe.models.ElementClassification;
import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.services.GrammarService;
import java.util.ArrayList;
import java.util.List;

public class DerivationTreeValidation {

  private final GrammarService grammarService;
  private final List<Boolean> correctDerivations = new ArrayList<>();
  private List<GrammarProduction> grammarRules;

  public DerivationTreeValidation(String grammarAsJson) {
    this.grammarService = new GrammarService(grammarAsJson);
  }

  public DerivationTreeValidation(Grammar grammar) {
    this.grammarService = new GrammarService(grammar);
  }

  public DetailResult checkTree(DerivationTree root, String word) {
    if (isStartSymbol(root)) {
      grammarRules = grammarService.getGrammarRules();
      DetailResult detailResult = checkDerivationTreeForCorrectSubstitutions(root);
      if (!detailResult.isCorrect()){
        return detailResult;
      }
      return derivedCorrectWord(root, word);
    } else {
      return new DetailResult(false, String.format("%s ist nicht das Startsymbol der Grammatik.", root.getContent()));
    }
  }

  private DetailResult derivedCorrectWord(DerivationTree root, String word) {
    StringBuilder derivedWord = new StringBuilder();

    root.getLeafNodes().forEach(leaf -> {
      derivedWord.append(leaf.getContent());
    });
    if (word == null || word.isBlank()) {
      return new DetailResult(false, "Das abzuleitende Wort ist leer.");
    } else if (!removeEpsilons(derivedWord.toString()).equals(word)) {
      return new DetailResult(false, String.format("Das abgeleitete Wort %s entspricht nicht dem Ausgangswort %s", removeEpsilons(derivedWord.toString()), word));
    }
    return new DetailResult(true);
  }

  private String removeEpsilons(String word) {
    return word.replace("epsilon", "");
  }

  private boolean isStartSymbol(DerivationTree root) {
    return root.getContent().equals(grammarService.getStartSymbol());
  }

  private DetailResult checkDerivationTreeForCorrectSubstitutions(DerivationTree element) {
    if (isNotTerminal(element)) {
      String rightGrammarSide =
          leftSideExists(element.getContent()) ? buildChildConcatination(element.getChildren())
              : "";

      if (isRuleInGrammar(element.getContent(), rightGrammarSide)) {
        for (DerivationTree child : element.getChildren()) {
          DetailResult detailResult = checkDerivationTreeForCorrectSubstitutions(child);
          if (!detailResult.isCorrect()) {
            return detailResult;
          }
        }
        return new DetailResult(true);
      } else {
        GrammarProduction wrongProduction = new GrammarProduction(element.getContent(), rightGrammarSide);
        return new DetailResult(false,
            wrongProduction, String.format("Die Produktion %s existiert nicht in der Grammatik.", wrongProduction));
      }
    }
    if (isTerminal(element)) {
      if (!element.getChildren().isEmpty()) {
        return new DetailResult(false, new GrammarProduction(element.getContent(), buildChildConcatination(element.getChildren())),
            String.format("Das Terminal %s darf keine Kinder haben.", element.getContent()));
      }
      return new DetailResult(true);
    } else {
      return new DetailResult(false, new GrammarProduction(element.getContent(), buildChildConcatination(element.getChildren())),
          String.format("Bei dem Element %s muss entweder ein Terminal oder Nichtterminal sein.", element.getContent()));
    }
  }

  private boolean leftSideExists(String content) {
    boolean leftSideExists = false;
    for (GrammarProduction grammarRule : grammarRules) {
      if (grammarRule.leftSide().equals(content)) {
        leftSideExists = true;
        break;
      }
    }
    return leftSideExists;
  }

  private String buildChildConcatination(List<DerivationTree> children) {
    StringBuilder rightSide = new StringBuilder();
    for (DerivationTree child : children) {
      rightSide.append(child.getContent());
    }
    return rightSide.toString().trim();
  }

  private boolean isTerminal(DerivationTree element) {
    return element.getClassification() == ElementClassification.TERMINAL;
  }

  private boolean isNotTerminal(DerivationTree element) {
    return element.getClassification() == ElementClassification.NON_TERMINAL;
  }

  private boolean isRuleInGrammar(String leftGrammarSide, String rightGrammarSide) {
    boolean correctGrammarRule = false;

    for (GrammarProduction grammarRule : grammarRules) {
      if (grammarRule.leftSide().equals(leftGrammarSide) && grammarRule.rightSide()
          .equals(rightGrammarSide)) {
        correctGrammarRule = true;
        break;
      }
    }
    return correctGrammarRule;
  }
}
