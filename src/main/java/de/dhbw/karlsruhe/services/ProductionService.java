package de.dhbw.karlsruhe.services;

import de.dhbw.karlsruhe.models.GrammarProduction;

import java.util.ArrayList;
import java.util.List;

public class ProductionService {

    public boolean isEndProduction(GrammarProduction production){
        boolean containsOnlyTerminals = true;
        for(Character c: production.rightSide().toCharArray()){
            if (Character.isUpperCase(c))
                containsOnlyTerminals = false;
        }
        return containsOnlyTerminals;
    }

    public List<String> getRightSideNonTerminals(GrammarProduction production){
        if (isEndProduction(production))
            return null;
        List<String> allNonTerminals = new ArrayList<>();
        for(Character c: production.rightSide().toCharArray()){
            if (Character.isUpperCase(c))
                allNonTerminals.add(c.toString());
        }
        return allNonTerminals;
    }

    public GrammarProduction removeSpaces(GrammarProduction production){
        return new GrammarProduction(production.leftSide(),production.rightSide().replaceAll("\\s+",""));
    }
}
