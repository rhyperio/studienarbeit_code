package de.dhbw.karlsruhe.models;

import java.util.ArrayList;
import java.util.List;

public record GrammarProduction(String leftSide, String rightSide) {
    
    @Override
    public String toString() {
        return  leftSide + "->" + rightSide;
    }

}
