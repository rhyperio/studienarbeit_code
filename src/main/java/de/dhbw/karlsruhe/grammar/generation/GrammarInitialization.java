package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.GrammarRule;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GrammarInitialization {
    private List<GrammarRule> grammarRules;
    private String[] terminals;
    private String[] nonTerminals;
    private String alphabetTerminals = "abcdefghijklmnopqrstuvwxyz0123456789";
    private String alphabetNonTerminals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private Random random = new Random();

    public void startGeneration () {
        int anzTerminals = this.random.nextInt(4) + 1;
        int anzNonTerminals = this.random.nextInt(7) +1;

        this.addTerminals(anzTerminals);
        this.addNonTerminals(anzNonTerminals);

        System.out.println("Terminals: " + terminals);
        System.out.println("Non Terminals: " + nonTerminals);
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
}
