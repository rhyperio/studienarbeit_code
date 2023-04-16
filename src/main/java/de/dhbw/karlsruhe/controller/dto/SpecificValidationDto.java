package de.dhbw.karlsruhe.controller.dto;

import de.dhbw.karlsruhe.derivation.specificDerivation.models.DerivationDirection;
import de.dhbw.karlsruhe.models.Grammar;
import java.util.List;

public class SpecificValidationDto {

  private DerivationDirection direction;
  private Grammar grammar;
  private List<String> derivationList;
  private String word;

  public SpecificValidationDto() {

  }

  public DerivationDirection getDirection() {
    return direction;
  }

  public Grammar getGrammar() {
    return grammar;
  }

  public List<String> getDerivationList() {
    return derivationList;
  }

  public String getWord() {
    return word;
  }
}
