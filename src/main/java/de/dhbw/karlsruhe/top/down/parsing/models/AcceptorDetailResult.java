package de.dhbw.karlsruhe.top.down.parsing.models;

import de.dhbw.karlsruhe.bottom.up.models.BottomUpStep;

import java.util.Objects;

public class AcceptorDetailResult {
    private boolean correct = false;
    private TopDownStep wrongStep = new TopDownStep();
    private String message = "";

    public AcceptorDetailResult() {
        // Needed for json deserialization
    }

    public AcceptorDetailResult(boolean correct) {
        this.correct = correct;
    }

    public AcceptorDetailResult(boolean correct, String message) {
        this.correct = correct;
        this.message = message;
    }

    public AcceptorDetailResult(boolean correct, TopDownStep wrongStep, String message) {
        this.correct = correct;
        this.wrongStep = wrongStep;
        this.message = message;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setWrongStep(TopDownStep wrongStep) {
        this.wrongStep = wrongStep;
    }

    public void setMessage (String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AcceptorDetailResult that)) return false;
        return correct == that.correct && Objects.equals(wrongStep, that.wrongStep) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correct, wrongStep, message);
    }
}
