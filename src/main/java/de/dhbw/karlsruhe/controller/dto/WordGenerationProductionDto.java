package de.dhbw.karlsruhe.controller.dto;

import de.dhbw.karlsruhe.models.Grammar;

public class WordGenerationProductionDto {

  private Grammar grammar;
  private int maxProductionCount;

  public WordGenerationProductionDto() {

  }

  public Grammar getGrammar() {
    return grammar;
  }

  public int getMaxProductionCount() {
    return maxProductionCount;
  }
}
