package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarRule;

import java.util.*;

public class GrammarGeneration {
    private List<GrammarRule> grammarRules = new ArrayList<>();
    private Grammar grammar;
    private String[] terminals;
    private String[] nonTerminals;
    private String alphabetTerminals = "abcdefghijklmnopqrstuvwxyz0123456789";
    private String alphabetNonTerminals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private Random random = new Random();

    public Grammar generateGrammar() {
        this.startGeneration();

        this.grammar = new Grammar(this.terminals, this.nonTerminals, this.grammarRules, this.nonTerminals[0]);

        return this.grammar;
    }

    private void startGeneration () {
        int anzTerminals = this.random.nextInt(4) + 1;
        int anzNonTerminals = this.random.nextInt(7) +1;

        this.addTerminals(anzTerminals);
        this.addNonTerminals(anzNonTerminals);
        this.generateProductions();

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
        for (GrammarRule gr : grammarRules) {
            System.out.println(gr.leftSide() + "->" + gr.rightSide());
        }
    }

    private void addNonTerminals(int anzNonTerminals) {
        Set<String> nonTerminalsSet = new HashSet<>();
        char nonTerminal;

        do {
            nonTerminal = alphabetNonTerminals.charAt(random.nextInt(alphabetNonTerminals.length()));
            nonTerminalsSet.add(Character.toString(nonTerminal));
        } while(nonTerminalsSet.size() < anzNonTerminals);

        this.nonTerminals = nonTerminalsSet.toArray(String[]::new);
    }

    private void addTerminals(int anzTerminals) {
        Set<String> terminalsSet = new HashSet<>();
        char terminal;

        do {
            terminal = alphabetTerminals.charAt(random.nextInt(alphabetTerminals.length()));
            terminalsSet.add(Character.toString(terminal));
        } while(terminalsSet.size() < anzTerminals);

        this.terminals = terminalsSet.toArray(String[]::new);
    }

    private void generateProductions() {
        for (String nonTerminal : nonTerminals) {
            int anzProductions = random.nextInt(4) +1 ;

            for (int i = 0; i < anzProductions; i++) {
                String rightSide = generaterightSide();
                GrammarRule gr = new GrammarRule(nonTerminal, rightSide);
                this.grammarRules.add(gr);
            }
        }
    }

    private String generaterightSide() {
        String rightSide = "";
        int lengthOfRS = random.nextInt(this.terminals.length + this.nonTerminals.length) + 1;

        for (int i = 0; i < lengthOfRS; i++) {
            int terminalOrNonTemrinal = random.nextInt(2);

            if (terminalOrNonTemrinal == 0) {
                // add Terminal
                String choosenTerminal = this.terminals[random.nextInt(this.terminals.length)];
                rightSide += choosenTerminal;
            } else {
                // add non Terminal
                String choosenNonTerminal = this.nonTerminals[random.nextInt(this.nonTerminals.length)];
                rightSide += choosenNonTerminal;
            }
        }

        return rightSide;
    }
}
