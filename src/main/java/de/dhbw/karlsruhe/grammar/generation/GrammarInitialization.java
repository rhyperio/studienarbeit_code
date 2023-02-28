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
    private Random random;

    public void startGeneration () {
        int anzTerminals = this.random.nextInt(4) + 1;
        int anzNonTerminals = this.random.nextInt(7) +1;

        this.addTerminals(anzTerminals);
        this.addNonTerminals(anzNonTerminals);

        System.out.println(terminals);
        System.out.println(nonTerminals);
    }

    private void addNonTerminals(int anzNonTerminals) {
        Set<String> nonTerminalsSet = new HashSet<>();
        char nonTerminal;

        do {
            nonTerminal = alphabetNonTerminals.charAt(random.nextInt());
            nonTerminalsSet.add(Character.toString(nonTerminal));
        } while(nonTerminalsSet.size() < anzNonTerminals);

        this.terminals = nonTerminalsSet.toArray(String[]::new);
    }

    private void addTerminals(int anzTerminals) {
        Set<String> terminalsSet = new HashSet<>();
        char terminal;

        do {
            terminal = alphabetTerminals.charAt(random.nextInt());
            terminalsSet.add(Character.toString(terminal));
        } while(terminalsSet.size() < anzTerminals);

        this.terminals = terminalsSet.toArray(String[]::new);
    }
}
