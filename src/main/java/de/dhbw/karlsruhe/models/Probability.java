package de.dhbw.karlsruhe.models;

public class Probability {

    private float probabilityForNewNonTerminal;
    private float probabilityForTerminal;
    private float probabilityForMultipleRightSide;
    private float probabilityFavourNonTerminalForTerminalOnRightSide;
    private float probabilityFavourNonTerminalForTerminalInStartProduction;
    private float decreasingProbabilityFactor;

    //Default constructor for json deserialization
    public Probability() {
    }

    public Probability(float probabilityForNewNonTerminal,
                       float probabilityForTerminal,
                       float probabilityForMultipleRightSide,
                       float probabilityFavourNonTerminalForTerminalOnRightSide,
                       float probabilityFavourNonTerminalForTerminalInStartProduction,
                       float decreasingProbabilityFactor) {
        this.probabilityForNewNonTerminal = probabilityForNewNonTerminal;
        this.probabilityForTerminal = probabilityForTerminal;
        this.probabilityForMultipleRightSide = probabilityForMultipleRightSide;
        this.probabilityFavourNonTerminalForTerminalOnRightSide = probabilityFavourNonTerminalForTerminalOnRightSide;
        this.probabilityFavourNonTerminalForTerminalInStartProduction = probabilityFavourNonTerminalForTerminalInStartProduction;
        this.decreasingProbabilityFactor = decreasingProbabilityFactor;
    }

    public float getProbabilityForNewNonTerminal() {
        return probabilityForNewNonTerminal;
    }

    public float getProbabilityForTerminal() {
        return probabilityForTerminal;
    }

    public float getProbabilityForMultipleRightSide() {
        return probabilityForMultipleRightSide;
    }

    public float getProbabilityFavourNonTerminalForTerminalOnRightSide() {
        return probabilityFavourNonTerminalForTerminalOnRightSide;
    }

    public float getProbabilityFavourNonTerminalForTerminalInStartProduction() {
        return probabilityFavourNonTerminalForTerminalInStartProduction;
    }

    public float getDecreasingProbabilityFactor() {
        return decreasingProbabilityFactor;
    }

    public void decreaseProbabilityForNewNonTerminal() {
        this.probabilityForNewNonTerminal -= decreasingProbabilityFactor;
    }

    public void decreaseProbabilityForTerminal() {
        this.probabilityForTerminal -= decreasingProbabilityFactor;
    }

    public void decreaseProbabilityForMultipleRightSide() {
        this.probabilityForMultipleRightSide -= decreasingProbabilityFactor;
    }

    public void decreaseProbabilityFavourNonTerminalForTerminalOnRightSide() {
        this.probabilityFavourNonTerminalForTerminalOnRightSide -= decreasingProbabilityFactor;
    }

    public void decreaseProbabilityFavourNonTerminalForTerminalInStartProduction() {
        this.probabilityFavourNonTerminalForTerminalInStartProduction -= decreasingProbabilityFactor;
    }

    public void increaseProbabilityForTerminal() {
        this.probabilityForTerminal += decreasingProbabilityFactor;
    }

}
