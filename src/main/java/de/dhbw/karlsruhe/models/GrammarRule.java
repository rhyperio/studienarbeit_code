package de.dhbw.karlsruhe.models;

public class GrammarRule {

  private String leftSide;
  private String rightSide;

  public GrammarRule(String leftSide, String rightSide) {
    this.leftSide = leftSide;
    this.rightSide = rightSide;
  }

  public String getLeftSide() {
    return leftSide;
  }

  public String getRightSide() {
    return rightSide;
  }
}
