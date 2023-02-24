package de.dhbw.karlsruhe.models;

import java.util.*;

public class ProductionSet {
    Set<GrammarRule> grammarRules = new HashSet<>();

    public ProductionSet(GrammarRule startRule){
        grammarRules.add(startRule);
    }

    public ProductionSet(List<GrammarRule> grList){
        grammarRules.addAll(grList);
    }

    public void addProduction(GrammarRule gr){
        if (isOnRightSide(gr.leftSide()) || isOnLeftSide(gr.leftSide())){
            grammarRules.add(gr);
        }
    }

    public void addProductionInReverse(GrammarRule gr){
        List<String> nonTerminals = gr.getRightSideNonTerminal();

        boolean allPresent = true;
        if (nonTerminals!= null) {
            for (String nonTerminal : nonTerminals) {
                if (!isOnLeftSide(nonTerminal)) {
                    allPresent = false;
                    break;
                }
            }
        }
        if (allPresent) {
            grammarRules.add(gr);
        }
    }

    public boolean isRuleInSet(GrammarRule gr){
        return grammarRules.contains(gr);
    }

    public int size(){
        return grammarRules.size();
    }

    // In case no right side terminals exist, returns start nonterminal
    public String getRandomRightSideNonTerminal(){

        List<String> nonTerminal = new ArrayList<>();
        for (GrammarRule gr: grammarRules){
            nonTerminal.add(gr.rightSide().substring(gr.rightSide().length() - 1));
        }
        nonTerminal = nonTerminal.stream().filter(c -> Character.isUpperCase(c.charAt(0))).toList();

        if (nonTerminal.isEmpty()){
            return grammarRules.stream().toList().get(0).leftSide();
        }

        Random rand = new Random();
        return nonTerminal.get(rand.nextInt(nonTerminal.size()));

    }

    public boolean isOnRightSide(String nonTerminal){
        for (GrammarRule gr : grammarRules){
            if (gr.rightSide().contains(nonTerminal)){
                return true;
            }
        }
        return false;
    }

    private boolean isOnLeftSide(String nonTerminal){
        for (GrammarRule gr : grammarRules){
            if (gr.leftSide().contains(nonTerminal)){
                return true;
            }
        }
        return false;
    }
}
