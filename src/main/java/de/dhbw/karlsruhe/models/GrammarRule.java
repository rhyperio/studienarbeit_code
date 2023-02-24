package de.dhbw.karlsruhe.models;

import java.util.ArrayList;
import java.util.List;

public record GrammarRule(String leftSide, String rightSide) {
    @Override
    public String toString() {
        return  leftSide + "->" + rightSide;
    }

    public boolean isEndProduction(){
        boolean b = true;
        for(Character c: rightSide.toCharArray()){
            if (Character.isUpperCase(c))
                b = false;
        }
        return b;
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
