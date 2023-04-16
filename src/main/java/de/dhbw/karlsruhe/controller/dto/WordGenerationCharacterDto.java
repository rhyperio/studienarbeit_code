package de.dhbw.karlsruhe.controller.dto;

import de.dhbw.karlsruhe.models.Grammar;

public class WordGenerationCharacterDto {

  private Grammar grammar;
  private int minWordLength;
  private int maxWordLength;
  private int maxProductionCount;

  public WordGenerationCharacterDto() {

  }

  public Grammar getGrammar() {
    return grammar;
  }

  public int getMinWordLength() {
    return minWordLength;
  }

  public int getMaxWordLength() {
    return maxWordLength;
  }

  public int getMaxProductionCount() {
    return maxProductionCount;
  }

}
