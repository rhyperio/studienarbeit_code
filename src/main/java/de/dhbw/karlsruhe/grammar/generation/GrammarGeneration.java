package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarRule;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;

public class GrammarGeneration {
    // ToDo: Verhindern von Grammatiken, wo nicht alle Terminale erreicht werden
    private Set<GrammarRule> grammarRulesSet;
    private List<GrammarRule> grammarRules;
    private String[] terminals;
    private String[] nonTerminals;
    private Random random = new Random();

    public Grammar generateGrammar() {
        GrammarVerification grammarVerification = new GrammarVerification();

        do {
            this.startGeneration();
        } while (!grammarVerification.verifyProductions(this.grammarRules));

        return new Grammar(this.terminals, this.nonTerminals, this.grammarRules, this.nonTerminals[0]);
    }

    private void startGeneration () {
        this.grammarRulesSet = new HashSet<>();
        int anzTerminals = this.random.nextInt(4) + 1;
        int anzNonTerminals = this.random.nextInt(7) +1;

        this.addTerminals(anzTerminals);
        this.addNonTerminals(anzNonTerminals);
        this.generateProductions();
        this.grammarRules = this.grammarRulesSet.stream().toList();

        // Output for Test
        System.out.println("Anz Terminals: " + this.terminals.length);
        System.out.println("Anz NonTerminals: " + this.nonTerminals.length);

        System.out.println("Terminals:");
        for (String term : terminals) {
            System.out.println(term);
        }

        System.out.println("Non Terminals:");
        for (String term : nonTerminals) {
            System.out.println(term);
        }

        System.out.println("GrammarRules:");
        for (GrammarRule gr : grammarRulesSet) {
            System.out.println(gr.leftSide() + "->" + gr.rightSide());
        }
    }

    private void addNonTerminals(int pAnzNonTerminals) {
        Set<String> nonTerminalsSet = new HashSet<>();
        char nonTerminal;

        do {
            nonTerminal = RandomStringUtils.random(1, true, false).toUpperCase().charAt(0);
            nonTerminalsSet.add(Character.toString(nonTerminal));
        } while(nonTerminalsSet.size() < pAnzNonTerminals);

        this.nonTerminals = nonTerminalsSet.toArray(String[]::new);
    }

    private void addTerminals(int pAnzTerminals) {
        Set<String> terminalsSet = new HashSet<>();
        char terminal;

        do {
            terminal = RandomStringUtils.random(1, true, true).toLowerCase().charAt(0);
            terminalsSet.add(Character.toString(terminal));
        } while(terminalsSet.size() < pAnzTerminals);

        this.terminals = terminalsSet.toArray(String[]::new);
    }

    private void generateProductions() {
        String rightSide = "";

        for (String nonTerminal : nonTerminals) {
            int anzProductions = this.random.nextInt(4) +1 ;

            do {
                do {
                    rightSide = generateRightSide();
                } while (rightSide.equalsIgnoreCase(nonTerminal));

                GrammarRule gr = new GrammarRule(nonTerminal, rightSide);
                this.grammarRulesSet.add(gr);
            } while (this.grammarRulesSet.size() < anzProductions);
        }
    }

    private String generateRightSide() {
        String rightSide = "";
        int lengthOfRightSide = this.random.nextInt(this.terminals.length + this.nonTerminals.length) + 1;

        for (int i = 0; i < lengthOfRightSide; i++) {
            if (this.random.nextFloat() <= 0.5) {
                // add Terminal
                String choosenTerminal = this.terminals[this.random.nextInt(this.terminals.length)];
                rightSide += choosenTerminal;
            } else {
                // add non Terminal
                String choosenNonTerminal = this.nonTerminals[this.random.nextInt(this.nonTerminals.length)];
                rightSide += choosenNonTerminal;
            }
        }

        return rightSide;
    }
}
