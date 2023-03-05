package de.dhbw.karlsruhe.models;

import java.util.ArrayList;
import java.util.List;

public record GrammarProduction(String leftSide, String rightSide) {
    
    @Override
    public String toString() {
        return  leftSide + "->" + rightSide;
    }

    public boolean isEndProduction(){
        boolean containsOnlyTerminals = true;
        for(Character c: rightSide.toCharArray()){
            if (Character.isUpperCase(c))
                containsOnlyTerminals = false;
        }
        return containsOnlyTerminals;
    }

    public List<String> getRightSideNonTerminal(){
        if (isEndProduction())
            return null;
        List<String> allNonTerminals = new ArrayList<>();
        for(Character c: rightSide.toCharArray()){
            if (Character.isUpperCase(c))
                allNonTerminals.add(c.toString());
        }
        return allNonTerminals;
    }
}
