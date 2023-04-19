package de.dhbw.karlsruhe.chomsky.generation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;

public class ChomskyDirectGeneration {

    private Set<GrammarProduction> chomskyProductionsSet;
    private List<GrammarProduction> chomskyProductions;
    private String[] terminals;
    private String[] nonTerminals;
    private String startNonTerminal;
    private Random random = new Random();

    public ChomskyDirectGeneration() {
    }

    public Grammar generateDirectChomskyGrammar() {
        this.chomskyProductions = new ArrayList<>();

        int anzTerminals = this.random.nextInt(4) + 1;
        int anzNonTerminals = this.random.nextInt(7) +1;

        this.generateTerminals(anzTerminals);
        this.generateNonTerminals(anzNonTerminals);
        this.startNonTerminal = this.nonTerminals[0];
        this.chomskyProductions = this.generateChomskyProductions();

        return new Grammar(this.terminals, this.nonTerminals, this.chomskyProductions.toArray(GrammarProduction[]::new), this.startNonTerminal);

    }

    private void generateTerminals(int anzTerminals) {
        Set<String> terminalsSet = new HashSet<>();
        char terminal;

        do {
            terminal = RandomStringUtils.random(1, true, true).toLowerCase().charAt(0);
            terminalsSet.add(Character.toString(terminal));
        } while(terminalsSet.size() < anzTerminals);

        this.terminals = terminalsSet.toArray(String[]::new);
    }

    private void generateNonTerminals(int anzNonTerminals) {
        Set<String> nonTerminalsSet = new HashSet<>();
        char nonTerminal;

        do {
            nonTerminal = RandomStringUtils.random(1, true, false).toUpperCase().charAt(0);
            nonTerminalsSet.add(Character.toString(nonTerminal));
        } while(nonTerminalsSet.size() < anzNonTerminals);

        this.nonTerminals = nonTerminalsSet.toArray(String[]::new);
    }

    private List<GrammarProduction> generateChomskyProductions() {
        this.chomskyProductionsSet = new HashSet<>();

        for (String nonTerminal : this.nonTerminals) {
            int anzProductions = this.random.nextInt(3) + 1;

            for (int i = 0; i < anzProductions; i++) {
                this.chomskyProductionsSet.add(new GrammarProduction(nonTerminal, this.generateRightSide()));
            }
        }

        if (this.random.nextFloat() <= 0.5) {
            this.addEpsilonToGrammar();
        }

        return this.chomskyProductionsSet.stream().toList();
    }

    private String generateRightSide() {
    }

    private void addEpsilonToGrammar() {
    }
}
