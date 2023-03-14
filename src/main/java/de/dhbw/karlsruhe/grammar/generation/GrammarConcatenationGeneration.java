package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;

public class GrammarConcatenationGeneration {
    // ToDo: Epsillon in Grammatik mitaufnehmen
    private Set<GrammarProduction> grammarRulesSet;
    private List<GrammarProduction> grammarRules;
    private String[] terminals;
    private String[] nonTerminals;
    private Random random = new Random();
    private GrammarVerification grammarVerification;

    public Grammar generateGrammar() {
        grammarVerification = new GrammarVerification();

        do {
            this.startGeneration();
        } while (!grammarVerification.verifyProductions(this.grammarRules, this.nonTerminals));

        return new Grammar(this.terminals, this.nonTerminals, this.grammarRules.toArray(GrammarProduction[]::new), this.nonTerminals[0]);
    }

    private void startGeneration () {
        this.grammarRulesSet = new HashSet<>();
        int anzTerminals = this.random.nextInt(4) + 1;
        int anzNonTerminals = this.random.nextInt(7) +1;

        this.addTerminals(anzTerminals);
        this.addNonTerminals(anzNonTerminals);
        this.generateProductions();
        this.grammarRules = this.grammarRulesSet.stream().toList();
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
            int setSizeBeforeCurrentGeneration = this.grammarRulesSet.size();
            do {
                do {
                    rightSide = generateRightSide();
                } while (rightSide.equalsIgnoreCase(nonTerminal));

                GrammarProduction gr = new GrammarProduction(nonTerminal, rightSide);
                this.grammarRulesSet.add(gr);
            } while ((this.grammarRulesSet.size() - setSizeBeforeCurrentGeneration) < anzProductions);
        }

        Set<String> notTerminatingNonTerminals = this.grammarVerification.getNonTerminatingNonTerminals(this.grammarRulesSet, this.nonTerminals);

        for (String notTerminatingNonTerminal : notTerminatingNonTerminals) {
            String terminatingRightSide = this.terminals[this.random.nextInt(this.terminals.length)];
            GrammarProduction terminatingGr = new GrammarProduction(notTerminatingNonTerminal, terminatingRightSide);
            this.grammarRulesSet.add(terminatingGr);
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
