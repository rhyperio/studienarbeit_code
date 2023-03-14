package de.dhbw.karlsruhe.derivation.tree.validation;

import de.dhbw.karlsruhe.derivation.tree.models.DerivationTree;
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

  public boolean checkTree(DerivationTree root, String word) {
    if (isStartSymbol(root)) {
      grammarRules = grammarService.getGrammarRules();
      checkDerivationTreeForCorrectSubstitutions(root);
      return !correctDerivations.contains(false) && derivedCorrectWord(root, word);
    } else {
      return false;
    }
  }

  private boolean derivedCorrectWord(DerivationTree root, String word) {
    StringBuilder derivedWord = new StringBuilder();

    root.getLeafNodes().forEach(leaf -> {
      derivedWord.append(leaf.getContent());
    });

    return removeEpsilons(derivedWord.toString()).equals(word) && !word.isBlank();
  }

  private String removeEpsilons(String word) {
    return word.replace("epsilon", "");
  }

  private boolean isStartSymbol(DerivationTree root) {
    return root.getContent().equals(grammarService.getStartSymbol());
  }

  private boolean checkDerivationTreeForCorrectSubstitutions(DerivationTree element) {
    if (isNotTerminal(element)) {
      String rightGrammarSide =
          leftSideExists(element.getContent()) ? buildChildConcatination(element.getChildren())
              : "";

      if (isRuleInGrammar(element.getContent(), rightGrammarSide)) {
        boolean correctDerivation = false;
        for (DerivationTree child : element.getChildren()) {
          correctDerivation = checkDerivationTreeForCorrectSubstitutions(child);
        }
        correctDerivations.add(correctDerivation);
        return correctDerivation;
      } else {
        correctDerivations.add(false);
        return false;
      }
    }
    boolean correctEnd = isTerminal(element) && element.getChildren().isEmpty();
    correctDerivations.add(correctEnd);
    return correctEnd;
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
