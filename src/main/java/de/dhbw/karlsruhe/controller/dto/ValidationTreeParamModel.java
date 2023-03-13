package de.dhbw.karlsruhe.controller.dto;

import de.dhbw.karlsruhe.derivation.tree.models.DerivationTree;
import de.dhbw.karlsruhe.models.Grammar;

public class ValidationTreeParamModel {

  private Grammar grammar;
  private DerivationTree derivationTree;
  private String word;

  public ValidationTreeParamModel() {
  }

  public Grammar getGrammar() {
    return grammar;
  }

  public DerivationTree getDerivationTree() {
    return derivationTree;
  }

  public String getWord() {
    return word;
  }
}
