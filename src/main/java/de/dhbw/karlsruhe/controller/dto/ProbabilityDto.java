package de.dhbw.karlsruhe.controller.dto;

import de.dhbw.karlsruhe.models.Probability;

public class ProbabilityDto {

    private float probabilityForNewNonTerminal;
    private float probabilityForTerminal;
    private float probabilityForMultipleRightSide;
    private float probabilityFavourNonTerminalForTerminalOnRightSide;
    private float probabilityFavourNonTerminalForTerminalInStartProduction;
    private float decreasingProbabilityFactor;

    public ProbabilityDto() {
    }

    public Probability fromDto() {
        return new Probability(
                probabilityForNewNonTerminal,
                probabilityForTerminal,
                probabilityForMultipleRightSide,
                probabilityFavourNonTerminalForTerminalOnRightSide,
                probabilityFavourNonTerminalForTerminalInStartProduction,
                decreasingProbabilityFactor);
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
}
