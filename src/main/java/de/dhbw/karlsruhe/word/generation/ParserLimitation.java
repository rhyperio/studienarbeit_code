package de.dhbw.karlsruhe.word.generation;

public class ParserLimitation {
    private int maxReadCount;
    private int maxActionCount;

    public ParserLimitation(int maxReadCount, int maxActionCount) {
        this.maxReadCount = maxReadCount;
        this.maxActionCount = maxActionCount;
    }

    public int getMaxReadCount() {
        return maxReadCount;
    }

    public int getMaxActionCount() {
        return maxActionCount;
    }
}
