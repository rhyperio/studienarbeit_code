package de.dhbw.karlsruhe.derivation.tree.models;

import de.dhbw.karlsruhe.models.GrammarProduction;
import java.util.Objects;

public class DetailResult {

  private boolean correct = false;
  private GrammarProduction wrongProduction;
  private String message = "";


  public DetailResult() {
    // Needed for json deserialization
  }

  public DetailResult(boolean correct) {
    this.correct = correct;
  }

  public DetailResult(boolean correct, String message) {
    this.correct = correct;
    this.message = message;
  }

  public DetailResult(boolean correct, GrammarProduction wrongProduction, String message) {
    this.correct = correct;
    this.wrongProduction = wrongProduction;
    this.message = message;
  }

  public boolean isCorrect() {
    return correct;
  }

  public GrammarProduction getWrongProduction() {
    return wrongProduction;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DetailResult that = (DetailResult) o;
    if (wrongProduction == null && that.wrongProduction == null) {
      return correct == that.correct && message.equals(that.message);
    }
    return correct == that.correct && wrongProduction.equals(that.wrongProduction) && message.equals(that.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(correct, wrongProduction, message);
  }
}
