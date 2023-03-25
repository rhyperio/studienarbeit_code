package de.dhbw.karlsruhe.bottom.up.models;

import de.dhbw.karlsruhe.models.GrammarProduction;

import java.util.Objects;

public class AcceptorDetailResult {
    private boolean correct = false;
    private BottomUpStep wrongStep;
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

    public AcceptorDetailResult(boolean correct, BottomUpStep wrongStep, String message) {
        this.correct = correct;
        this.wrongStep = wrongStep;
        this.message = message;
    }

    public boolean isCorrect() {
        return correct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AcceptorDetailResult that)) return false;

        if (wrongStep == null && that.wrongStep == null)
            return correct == that.correct && Objects.equals(message, that.message);

        return correct == that.correct && Objects.equals(wrongStep, that.wrongStep) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correct, wrongStep, message);
    }
}
