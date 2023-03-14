package de.dhbw.karlsruhe.models;

public class Probability {

    private float probabilityForNewNonTerminal;
    private float probabilityForTerminal;
    private float probabilityForMultipleRightSide;
    private float probabilityFavourNonTerminalForTerminalOnRightSide;
    private float probabilityFavourNonTerminalForTerminalInStartProduction;
    private float decreasingProbabilityFactor;

    private final float START_PROBABILITY_FOR_MULTIPLE_RIGHT_SIDE;
    private final float START_PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_ON_RIGHT_SIDE;
    private final float START_PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_IN_START_PRODUCTION;

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

        this.START_PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_IN_START_PRODUCTION = probabilityFavourNonTerminalForTerminalInStartProduction;
        this.START_PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_ON_RIGHT_SIDE = probabilityFavourNonTerminalForTerminalOnRightSide;
        this.START_PROBABILITY_FOR_MULTIPLE_RIGHT_SIDE = probabilityForMultipleRightSide;
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

    public float getSTART_PROBABILITY_FOR_MULTIPLE_RIGHT_SIDE() {
        return START_PROBABILITY_FOR_MULTIPLE_RIGHT_SIDE;
    }

    public float getSTART_PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_ON_RIGHT_SIDE() {
        return START_PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_ON_RIGHT_SIDE;
    }

    public float getSTART_PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_IN_START_PRODUCTION() {
        return START_PROBABILITY_FAVOUR_NON_TERMINAL_FOR_TERMINAL_IN_START_PRODUCTION;
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
