package de.dhbw.karlsruhe.models;

import de.dhbw.karlsruhe.services.ProductionService;

import java.util.*;

public class ProductionSet {
    private Set<GrammarProduction> productions = new HashSet<>();

    public ProductionSet(GrammarProduction startRule){
        productions.add(startRule);
    }

    public ProductionSet(List<GrammarProduction> productionList){
        productions.addAll(productionList);
    }

    public void addAllReachableProductions(List<GrammarProduction> allProductions) {
        int pSetsize = 0;
        int tmpSize = -1;
        while (pSetsize != tmpSize){
            tmpSize = this.size();
            for (GrammarProduction gr : allProductions) {
                this.addProduction(gr);
            }
            pSetsize = this.size();
        }
    }

    /**
     * Returns true if production is in the set after adding
     */
    public boolean addProduction(GrammarProduction production){
        if (isOnRightSide(production.leftSide()) || isOnLeftSide(production.leftSide())){
            productions.add(production);
        }
        return this.isRuleInSet(production);
    }

    /**
     * Returns true if production is in the set after adding
     */
    public boolean addProductionInReverse(GrammarProduction production){
        ProductionService productionService = new ProductionService();
        List<String> nonTerminals = productionService.getRightSideNonTerminals(production);

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
            productions.add(production);
        }

        return this.isRuleInSet(production);
    }

    public boolean isRuleInSet(GrammarProduction production){
        return productions.contains(production);
    }

    public int size(){
        return productions.size();
    }

    // In case no right side terminals exist, returns start nonterminal
    public String getRandomRightSideNonTerminal(){

        List<String> nonTerminal = new ArrayList<>();
        for (GrammarProduction production: productions){
            nonTerminal.add(production.rightSide().substring(production.rightSide().length() - 1));
        }
        nonTerminal = nonTerminal.stream().filter(c -> Character.isUpperCase(c.charAt(0))).toList();

        if (nonTerminal.isEmpty()){
            return productions.stream().toList().get(0).leftSide();
        }

        Random rand = new Random();
        return nonTerminal.get(rand.nextInt(nonTerminal.size()));

    }

    private boolean isOnRightSide(String nonTerminal){
        for (GrammarProduction production : productions){
            if (production.rightSide().contains(nonTerminal)){
                return true;
            }
        }
        return false;
    }

    private boolean isOnLeftSide(String nonTerminal){
        for (GrammarProduction production : productions){
            if (production.leftSide().contains(nonTerminal)){
                return true;
            }
        }
        return false;
    }
}
