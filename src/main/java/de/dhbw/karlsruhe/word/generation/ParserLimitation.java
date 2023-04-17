package de.dhbw.karlsruhe.word.generation;

public class ParserLimitation {
    int maxReadCount;
    int maxActionCount;

    public ParserLimitation(int maxReadCount, int maxActionCount) {
        this.maxReadCount = maxReadCount;
        this.maxActionCount = maxActionCount;
    }
}
