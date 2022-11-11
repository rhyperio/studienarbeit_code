package de.dhbw.karlsruhe.derivation.tree.models;

import de.dhbw.karlsruhe.models.ElementClassification;
import java.util.ArrayList;
import java.util.List;

public class DerivationTree {

  private String content;
  private ElementClassification classification;
  private List<DerivationTree> children = new ArrayList<>();

  public String getContent() {
    return content;
  }

  public ElementClassification getClassification() {
    return classification;
  }

  public List<DerivationTree> getChildren() {
    return children;
  }
}
