package de.dhbw.karlsruhe.chomsky.generation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.services.ProductionService;
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
        boolean epsilonIncluded = false;
        this.chomskyProductionsSet = new HashSet<>();

        if (this.random.nextFloat() <= 0.5) {
            this.chomskyProductionsSet.add(new GrammarProduction(this.startNonTerminal, "ε"));
            this.terminals = Arrays.copyOf(this.terminals, this.terminals.length + 1);
            this.terminals[this.terminals.length - 1] = "ε";
            epsilonIncluded = true;
        }

        for (String nonTerminal : this.nonTerminals) {
            int anzProductions = this.random.nextInt(3) + 1;

            for (int i = 0; i < anzProductions; i++) {
                this.chomskyProductionsSet.add(new GrammarProduction(nonTerminal, this.generateRightSide(nonTerminal, epsilonIncluded)));
            }
        }

        Set<String> nonTerminatingNonTerminals = this.getNonTerminatingNonTerminals();
        this.checkAtLeastOneEndProductionAndResolve(this.chomskyProductionsSet, this.terminals);
        this.resolveNonTerminatingNonTerminals(nonTerminatingNonTerminals);

        return this.chomskyProductionsSet.stream().toList();
    }

    private String generateRightSide(String leftSide, boolean epsilonIncluded) {
        if (this.random.nextFloat() <= 0.5) {
            // return terminal
            return this.getTerminalForRightSide(epsilonIncluded);
        } else {
            // return two nonTerminal
            return this.getTwoNonTermialsForRightSide(leftSide, epsilonIncluded);
        }
    }

    private String getTerminalForRightSide(boolean epsilonIncluded) {
        String terminalForRightSide;

        if (epsilonIncluded) {
            do {
                terminalForRightSide = this.terminals[this.random.nextInt(this.terminals.length)];
            } while (terminalForRightSide.equals("ε"));

            return terminalForRightSide;
        } else {
            return this.terminals[this.random.nextInt(this.terminals.length)];
        }
    }

    private String getTwoNonTermialsForRightSide(String leftSide, boolean epsilonIncluded) {
        String firstNonTerminal;
        String secondNonTerminal;

        firstNonTerminal = this.nonTerminals[this.random.nextInt(this.nonTerminals.length)];
        secondNonTerminal = this.nonTerminals[this.random.nextInt(this.nonTerminals.length)];

        if (epsilonIncluded) {
            while (firstNonTerminal.equals(this.startNonTerminal) && this.nonTerminals.length > 1) {
                firstNonTerminal = this.nonTerminals[this.random.nextInt(this.nonTerminals.length)];
            }

            while (secondNonTerminal.equals(this.startNonTerminal) && this.nonTerminals.length > 1) {
                secondNonTerminal = this.nonTerminals[this.random.nextInt(this.nonTerminals.length)];
            }
        }

        return firstNonTerminal + secondNonTerminal;
    }

    private Set<String> getNonTerminatingNonTerminals() {
        Set<String> nonTerminatingNonTerminals = new HashSet<>();

        for (String currentNonTerminal : this.nonTerminals) {
            if (this.checkIfNonTerminalLoopsItself(currentNonTerminal)) {
                nonTerminatingNonTerminals.add(currentNonTerminal);
            }
        }

        return nonTerminatingNonTerminals;
    }

    private boolean checkIfNonTerminalLoopsItself(String nonTerminal) {
        List<String> leftSidesOfUsage = new ArrayList<>();
        StringBuilder rightSidesOfNonTerminalToCheck = new StringBuilder();

        for (GrammarProduction gr : this.chomskyProductionsSet) {
            if (gr.leftSide().equals(nonTerminal)) {
                rightSidesOfNonTerminalToCheck.append(gr.rightSide());
            }

            if (gr.rightSide().contains(nonTerminal) || this.checkIfRightSideContainsUsedNonTerminals(gr.rightSide(), leftSidesOfUsage)) {
                leftSidesOfUsage.add(gr.leftSide());
            }
        }

        if (leftSidesOfUsage.contains(nonTerminal) || this.checkIfRightSideContainsUsedNonTerminals(rightSidesOfNonTerminalToCheck.toString(), leftSidesOfUsage)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkIfRightSideContainsUsedNonTerminals(String rightSidesOfNonTerminalToCheck, List<String> leftSidesOfUsage) {
        for (int i = 0; i < leftSidesOfUsage.size(); i++) {
            if (rightSidesOfNonTerminalToCheck.contains(leftSidesOfUsage.get(i))) {
                return true;
            }
        }

        return false;
    }

    private void checkAtLeastOneEndProductionAndResolve(Set<GrammarProduction> chomskyProductionsSet, String[] terminals) {
        ProductionService productionService = new ProductionService();
        if (chomskyProductionsSet.stream().noneMatch(productionService::isEndProduction)) {
            this.addEndProduction();
        }
    }

    private void addEndProduction() {
        GrammarProduction gp = this.chomskyProductionsSet.stream().toList().get(this.chomskyProductionsSet.size()-1);
        String nonTerminal = gp.leftSide();
        String terminalRightSide;

        do {
            terminalRightSide = this.terminals[this.random.nextInt(this.terminals.length)];
        } while (terminalRightSide.equals("ε"));

        this.chomskyProductionsSet.add(new GrammarProduction(nonTerminal, terminalRightSide));
    }

    private void resolveNonTerminatingNonTerminals(Set<String> nonTerminatingNonTerminals) {
        String terminalRightSide;

        for (String nonTerminal : nonTerminatingNonTerminals) {
            do {
                terminalRightSide = this.terminals[this.random.nextInt(this.terminals.length)];
            } while (terminalRightSide.equals("ε"));

            this.chomskyProductionsSet.add(new GrammarProduction(nonTerminal, terminalRightSide));
        }
    }
}
