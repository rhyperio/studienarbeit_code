package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.*;

public class GrammarConcatenationGeneration extends GrammarGeneration{
    private Set<GrammarProduction> grammarRulesSet;
    private List<GrammarProduction> grammarRules;
    private String[] terminals;
    private String[] nonTerminals;
    private Random random = new Random();
    private GrammarConcatenationVerification grammarConcatenationVerification;

    public Grammar generateGrammar() {
        grammarConcatenationVerification = new GrammarConcatenationVerification();

        do {
            this.startGeneration();
        } while (!grammarConcatenationVerification.verifyProductions(this.grammarRules, this.nonTerminals, this.terminals));

        return new Grammar(this.terminals, this.nonTerminals, this.grammarRules.toArray(GrammarProduction[]::new), this.nonTerminals[0]);
    }

    private void startGeneration () {
        this.grammarRulesSet = new HashSet<>();
        int anzTerminals = this.random.nextInt(4) + 1;
        int anzNonTerminals = this.random.nextInt(7) +1;

        this.addTerminals(anzTerminals);
        this.addNonTerminals(anzNonTerminals);
        this.grammarRules = this.generateProductions();
    }

    private void addNonTerminals(int anzNonTerminals) {
        Set<String> nonTerminalsSet = new HashSet<>();
        char nonTerminal;

        do {
            nonTerminal = RandomStringUtils.random(1, true, false).toUpperCase().charAt(0);
            nonTerminalsSet.add(Character.toString(nonTerminal));
        } while(nonTerminalsSet.size() < anzNonTerminals);

        this.nonTerminals = nonTerminalsSet.toArray(String[]::new);
    }

    private void addTerminals(int anzTerminals) {
        Set<String> terminalsSet = new HashSet<>();
        char terminal;

        do {
            terminal = RandomStringUtils.random(1, true, true).toLowerCase().charAt(0);
            terminalsSet.add(Character.toString(terminal));
        } while(terminalsSet.size() < anzTerminals);

        this.terminals = terminalsSet.toArray(String[]::new);
    }

    protected List<GrammarProduction> generateProductions() {
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

        Set<String> notTerminatingNonTerminals = this.grammarConcatenationVerification.getNonTerminatingNonTerminals(this.grammarRulesSet, this.nonTerminals);

        String[] terminalsAndEpsilon = Arrays.copyOf(this.terminals, this.terminals.length + 1);
        terminalsAndEpsilon[terminalsAndEpsilon.length - 1] = "ε";


        for (String notTerminatingNonTerminal : notTerminatingNonTerminals) {
            String terminatingRightSide = terminalsAndEpsilon[this.random.nextInt(terminalsAndEpsilon.length)];
            GrammarProduction terminatingGp = new GrammarProduction(notTerminatingNonTerminal, terminatingRightSide);
            this.grammarRulesSet.add(terminatingGp);
            if (terminatingRightSide.equals("ε")) {
                this.terminals = terminalsAndEpsilon;
                String safeTerminatingRightSide = this.terminals[this.random.nextInt(this.terminals.length)];
                GrammarProduction safeTerminatingGr = new GrammarProduction(notTerminatingNonTerminal, safeTerminatingRightSide);
                this.grammarRulesSet.add(safeTerminatingGr);
            }
        }

        return this.grammarRulesSet.stream().toList();
    }

    private String generateRightSide() {
        StringBuilder rightSide = new StringBuilder();
        int lengthOfRightSide = this.random.nextInt(this.terminals.length + this.nonTerminals.length) + 1;

        for (int i = 0; i < lengthOfRightSide; i++) {
            if (this.random.nextFloat() <= 0.5) {
                // add Terminal
                String choosenTerminal = this.terminals[this.random.nextInt(this.terminals.length)];
                rightSide.append(choosenTerminal);
            } else {
                // add non Terminal
                String choosenNonTerminal = this.nonTerminals[this.random.nextInt(this.nonTerminals.length)];
                rightSide.append(choosenNonTerminal);
            }
        }

        return rightSide.toString();
    }
}
