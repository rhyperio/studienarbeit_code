package de.dhbw.karlsruhe.top.down.parsing.models;

import java.util.Objects;

public class TDAcceptorDetailResult {
    private boolean correct = false;
    private TopDownStep wrongStep = new TopDownStep();
    private String message = "";

    public TDAcceptorDetailResult() {
        // Needed for json deserialization
    }

    public TDAcceptorDetailResult(boolean correct) {
        this.correct = correct;
    }

    public TDAcceptorDetailResult(boolean correct, String message) {
        this.correct = correct;
        this.message = message;
    }

    public TDAcceptorDetailResult(boolean correct, TopDownStep wrongStep, String message) {
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

    public TopDownStep getWrongStep() {
        return wrongStep;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TDAcceptorDetailResult that)) return false;
        return correct == that.correct && Objects.equals(wrongStep, that.wrongStep) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correct, wrongStep, message);
    }
}
