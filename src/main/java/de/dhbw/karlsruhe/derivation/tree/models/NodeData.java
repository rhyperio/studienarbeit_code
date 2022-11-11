package de.dhbw.karlsruhe.derivation.tree.models;

public class NodeData {

  private String content;
  private ElementClassification classification;

  public NodeData(String content, ElementClassification classification) {
    this.content = content;
    this.classification = classification;
  }

  public String getContent() {
    return content;
  }

  public ElementClassification getClassification() {
    return classification;
  }

}
