package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarRule;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class GrammarGeneration {
    private List<GrammarRule> grammarRules;
    private Grammar grammar;
    private String[] terminals;
    private String[] nonTerminals;
    private Random random = new Random();
    private GrammarVerification grammarVerification;

    public Grammar generateGrammar() {
        this.grammarVerification = new GrammarVerification();

        do {
            this.startGeneration();
        } while (!this.grammarVerification.verifyProductions(this.grammarRules));

        this.grammar = new Grammar(this.terminals, this.nonTerminals, this.grammarRules, this.nonTerminals[0]);

        return this.grammar;
    }

    private void startGeneration () {
        this.grammarRules = new ArrayList<>();
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
        for (String nonTerminal : nonTerminals) {
            int anzProductions = random.nextInt(4) +1 ;

            for (int i = 0; i < anzProductions; i++) {
                String rightSide = generateRightSide();
                GrammarRule gr = new GrammarRule(nonTerminal, rightSide);
                this.grammarRules.add(gr);
            }
        }
    }

    private String generateRightSide() {
        String rightSide = "";
        int lengthOfRightSide = random.nextInt(this.terminals.length + this.nonTerminals.length) + 1;

        for (int i = 0; i < lengthOfRightSide; i++) {
            if (random.nextFloat() <= 0.5) {
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
