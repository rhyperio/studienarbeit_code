package de.dhbw.karlsruhe.controller.dto;

import de.dhbw.karlsruhe.models.Grammar;

public class WordReadActionLimitationDto {

    private Grammar grammar;
    private int maxReadCount;
    private int maxActionCount;

    public WordReadActionLimitationDto() {

    }

    public Grammar getGrammar() {
        return grammar;
    }

    public int getMaxReadCount() {
        return maxReadCount;
    }

    public int getMaxActionCount() {
        return maxActionCount;
    }
}
