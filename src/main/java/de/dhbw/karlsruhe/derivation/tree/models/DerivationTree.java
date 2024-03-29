package de.dhbw.karlsruhe.derivation.tree.models;

import de.dhbw.karlsruhe.models.ElementClassification;
import java.util.ArrayList;
import java.util.List;

public class DerivationTree {

  private final List<DerivationTree> children = new ArrayList<>();
  private String content;
  private ElementClassification classification;

  public String getContent() {
    return content;
  }

  public ElementClassification getClassification() {
    return classification;
  }

  public List<DerivationTree> getChildren() {
    return children;
  }

  public List<DerivationTree> getLeafNodes() {
    List<DerivationTree> leafNodes = new ArrayList<>();

    if (children.isEmpty()) {
      leafNodes.add(this);
    } else {
      for (DerivationTree child : children) {
        leafNodes.addAll(child.getLeafNodes());
      }
    }
    return leafNodes;
  }
}
