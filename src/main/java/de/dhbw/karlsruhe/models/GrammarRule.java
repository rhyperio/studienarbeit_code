package de.dhbw.karlsruhe.models;

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
}
