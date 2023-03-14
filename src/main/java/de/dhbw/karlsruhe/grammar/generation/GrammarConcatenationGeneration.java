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
        } while (!grammarConcatenationVerification.verifyProductions(this.grammarRules, this.nonTerminals));

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

        List<String> temp = new ArrayList<>(Arrays.stream(this.terminals).toList());
        temp.add("epsilon");
        String[] terminalsAndEpsilon = temp.toArray(new String[0]);


        for (String notTerminatingNonTerminal : notTerminatingNonTerminals) {
            String terminatingRightSide = terminalsAndEpsilon[this.random.nextInt(terminalsAndEpsilon.length)];
            GrammarProduction terminatingGr = new GrammarProduction(notTerminatingNonTerminal, terminatingRightSide);
            this.grammarRulesSet.add(terminatingGr);
        }

        return this.grammarRulesSet.stream().toList();
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
