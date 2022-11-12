package de.dhbw.karlsruhe.derivation.tree.validation;

import de.dhbw.karlsruhe.derivation.tree.models.DerivationTree;
import de.dhbw.karlsruhe.models.ElementClassification;
import de.dhbw.karlsruhe.models.GrammarRule;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DerivationTreeValidation {

  private final CollectGrammarRules collectGrammarRules;
  private List<GrammarRule> grammarRules;
  private List<Boolean> correctDerivations = new ArrayList<>();

  public DerivationTreeValidation(CollectGrammarRules collectGrammarRules) {
    this.collectGrammarRules = collectGrammarRules;
  }

  public boolean checkTree(DerivationTree root) {
    grammarRules = collectGrammarRules.getGrammarRules();
    checkDerivationTreeForCorrectSubstitutions(root);
    return !correctDerivations.contains(false);
  }

  private boolean checkDerivationTreeForCorrectSubstitutions(DerivationTree element) {

    if (isNotTerminal(element)) {
      AtomicBoolean leftSideExists = new AtomicBoolean(false);

      grammarRules.forEach(grammarRule -> {
        if (grammarRule.getLeftSide().equals(element.getContent())) {
          leftSideExists.set(true);
        }
      });

      StringBuilder rightGrammarSide = new StringBuilder();
      if (leftSideExists.get()) {
        element.getChildren().forEach(childElement -> {
          rightGrammarSide.append(childElement.getContent());
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
    correctDerivations.add(true);
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
      if (grammarRule.getLeftSide().equals(leftGrammarSide) && grammarRule.getRightSide()
          .equals(rightGrammarSide)) {
        correctGrammarRule.set(true);
      }
    });

    return correctGrammarRule.get();
  }

}
