package de.dhbw.karlsruhe.derivation.tree.validation;

import de.dhbw.karlsruhe.derivation.tree.ElementClassification;
import de.dhbw.karlsruhe.derivation.tree.GrammarRule;
import de.dhbw.karlsruhe.derivation.tree.NodeData;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DerivationTreeValidation {

  private List<GrammarRule> grammarRules = new ArrayList<>();

  public boolean checkTree(Node<NodeData> root) {
    return checkDerivationTreeForCorrectSubstitutions(root);
  }

  private boolean checkDerivationTreeForCorrectSubstitutions(Node<NodeData> element) {

    if (isNotTerminal(element)) {
      AtomicBoolean leftSideExists = new AtomicBoolean(false);

      grammarRules.forEach(grammarRule -> {
        if (grammarRule.getLeftSide().equals(element.getData().getContent())) {
          leftSideExists.set(true);
        }
      });

      StringBuilder rightGrammarSide = new StringBuilder();
      if (leftSideExists.get()) {
        element.getChildren().forEach(childElement -> {
          rightGrammarSide.append(childElement.getData().getContent());
        });
      }
      if (isRuleInGrammar(element.getData().getContent(), rightGrammarSide.toString())) {
        AtomicBoolean correctDerivation = new AtomicBoolean(false);
        element.getChildren().forEach(child -> {
          correctDerivation.set(checkDerivationTreeForCorrectSubstitutions(child));
        });
        return correctDerivation.get();
      } else {
        return false;
      }
    }
    return true;
  }

  private boolean isTerminal(Node<NodeData> element) {
    return element.getData().getClassification() == ElementClassification.TERMINAL;
  }

  private boolean isNotTerminal(Node<NodeData> element) {
    return element.getData().getClassification() == ElementClassification.NON_TERMINAL;
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
