package de.dhbw.karlsruhe.derivation.tree.validation;

import de.dhbw.karlsruhe.derivation.tree.models.DerivationTree;
import de.dhbw.karlsruhe.models.ElementClassification;
import de.dhbw.karlsruhe.models.GrammarRule;
import de.dhbw.karlsruhe.services.GrammarService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DerivationTreeValidation {

  private final GrammarService grammarService;
  private final List<Boolean> correctDerivations = new ArrayList<>();
  private List<GrammarRule> grammarRules;

  public DerivationTreeValidation(String grammarAsJson) {
    this.grammarService = new GrammarService(grammarAsJson);
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
      AtomicBoolean leftSideExists = new AtomicBoolean(false);

      grammarRules.forEach(grammarRule -> {
        if (grammarRule.leftSide().equals(element.getContent())) {
          leftSideExists.set(true);
        }
      });

      StringBuilder rightGrammarSide = new StringBuilder();
      if (leftSideExists.get()) {
        element.getChildren().forEach(childElement -> {
          rightGrammarSide.append(childElement.getContent()).append(" ");
        });
      }
      if (isRuleInGrammar(element.getContent(), rightGrammarSide.toString())) {
        AtomicBoolean correctDerivation = new AtomicBoolean(false);
        element.getChildren().forEach(child -> {
          correctDerivation.set(checkDerivationTreeForCorrectSubstitutions(child));
        });
        correctDerivations.add(correctDerivation.get());
        return correctDerivation.get();
      } else {
        correctDerivations.add(false);
        return false;
      }
    }
    correctDerivations.add(element.getChildren().isEmpty());
    return true;
  }

  private boolean isTerminal(DerivationTree element) {
    return element.getClassification() == ElementClassification.TERMINAL;
  }

  private boolean isNotTerminal(DerivationTree element) {
    return element.getClassification() == ElementClassification.NON_TERMINAL;
  }

  private boolean isRuleInGrammar(String leftGrammarSide, String rightGrammarSide) {
    AtomicBoolean correctGrammarRule = new AtomicBoolean(false);

    grammarRules.forEach(grammarRule -> {
      if (grammarRule.leftSide().equals(leftGrammarSide.trim()) && grammarRule.rightSide()
          .equals(rightGrammarSide.trim())) {
        correctGrammarRule.set(true);
      }
    });

    return correctGrammarRule.get();
  }

}
