package de.dhbw.karlsruhe.models;

public record GrammarRule(String leftSide, String rightSide) {
    @Override
    public String toString() {
        return  leftSide + "->" + rightSide;
    }
}
