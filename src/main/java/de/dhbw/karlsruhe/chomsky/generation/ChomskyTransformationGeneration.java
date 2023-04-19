package de.dhbw.karlsruhe.chomsky.generation;

import de.dhbw.karlsruhe.grammar.generation.GrammarConcatenationGeneration;
import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;

import java.util.*;
import java.util.stream.Collectors;

public class ChomskyTransformationGeneration {
    private Grammar typeTwoGrammar;
    private List<GrammarProduction> chomskyProductions;
    private List<GrammarProduction> modifiedChomskyProductions;
    private Set<String> chomskyNonTerminals;
    private Set<String> chomskyTerminals;
    private String chomskyStartSymbol;

    public ChomskyTransformationGeneration() {
    }

    public Grammar generateChomskyGrammar() {
        do {
            this.typeTwoGrammar = new GrammarConcatenationGeneration().generateGrammar();
        } while (Arrays.stream(this.typeTwoGrammar.getNonTerminals()).toList().contains("Z") || Arrays.stream(this.typeTwoGrammar.getNonTerminals()).toList().contains("V"));
        this.chomskyProductions = new ArrayList<>(List.of(this.typeTwoGrammar.getProductions()));
        this.chomskyNonTerminals = new HashSet<>(List.of(this.typeTwoGrammar.getNonTerminals()));
        this.chomskyTerminals = new HashSet<>(List.of(this.typeTwoGrammar.getTerminals()));
        this.chomskyStartSymbol = this.typeTwoGrammar.getStartSymbol();

        Set<String> epsG = this.getEPSFromGrammar();
        boolean epsIsInLanguage = epsG.contains(this.typeTwoGrammar.getStartSymbol());

        this.resolveEpsilonProduction(epsIsInLanguage);
        this.replaceTerminals();
        this.resolveNonTerminalsOnRightSide();
        this.resolveSingleProductions();

        this.grammarProductionCleanUp();

        return new Grammar(this.chomskyTerminals.toArray(new String[0]), this.chomskyNonTerminals.toArray(new String[0]), this.chomskyProductions.toArray(new GrammarProduction[0]), this.chomskyStartSymbol);
    }

    private Set<String> getEPSFromGrammar() {
        Set<String> m_current = new HashSet<>();
        Set<String> m_new;

        for (GrammarProduction gp : this.typeTwoGrammar.getProductions()) {
            if (gp.rightSide().equals("ε")) {
                m_current.add(gp.leftSide());
            }
        }

        m_new = new HashSet<>(m_current);

        do {
            m_current = new HashSet<>(m_new);
            m_new = new HashSet<>(m_current);

            for (GrammarProduction gp : this.typeTwoGrammar.getProductions()) {
                if (m_current.stream().anyMatch(gp.rightSide()::contains)) {
                    m_new.add(gp.leftSide());
                }
            }

        } while(!m_current.equals(m_new));

        return m_new;
    }

    private void replaceTerminals() {
        String[] terminals = this.typeTwoGrammar.getTerminals();

        for (String terminal : terminals) {
            if (terminal.equals("ε")) {
                continue;
            }

            String newNonTerminal = "Z_" + terminal;
            this.chomskyNonTerminals.add(newNonTerminal);
            this.replaceTerminalInProductions(terminal, newNonTerminal);
            this.chomskyProductions.add(new GrammarProduction(newNonTerminal, terminal));
        }
    }

    private void replaceTerminalInProductions(String terminal, String nonTerminal) {
        for (GrammarProduction gp : this.chomskyProductions) {
            if (gp.rightSide().contains(terminal)) {
                String newRightSide = gp.rightSide().replace(terminal, nonTerminal);
                this.chomskyProductions.set(this.chomskyProductions.indexOf(gp), new GrammarProduction(gp.leftSide(), newRightSide));
            }
        }
    }

    private void resolveEpsilonProduction(boolean epsIsInLanguage) {
        if (epsIsInLanguage) {
            String newStartSymbol = "S'";
            this.chomskyStartSymbol = newStartSymbol;
            this.chomskyProductions.add(new GrammarProduction(newStartSymbol, "ε"));
            this.chomskyProductions.add(new GrammarProduction(newStartSymbol, this.typeTwoGrammar.getStartSymbol()));

            this.removeEpsilonProductions();
        }
    }

    private void removeEpsilonProductions() {
        this.modifiedChomskyProductions = new ArrayList<>(this.chomskyProductions);

        for (GrammarProduction gp : this.chomskyProductions) {

            if (gp.rightSide().equals("ε") && !gp.leftSide().equals("S'")) {
                this.modifiedChomskyProductions.remove(gp);
                int anzProductions = this.getAnzProductionsOfLeftNonTerminal(gp.leftSide());

                if (anzProductions == 0) {
                    this.removeOccurenciesOfNonTerminal(gp.leftSide());
                } else {
                    this.detectOccurrencesAndResolveProductions(gp.leftSide());
                }
            }
        }

        this.chomskyProductions =  new ArrayList<>(this.modifiedChomskyProductions);
    }

    private int getAnzProductionsOfLeftNonTerminal(String leftSide) {
        int anzProductions = 0;

        for (GrammarProduction gp: this.chomskyProductions) {
            if (gp.leftSide().equals(leftSide)) {
                anzProductions += 1;
            }
        }

        return anzProductions;
    }

    private void removeOccurenciesOfNonTerminal(String leftSide) {
        List<GrammarProduction> tempProductions = new ArrayList<>(this.modifiedChomskyProductions);

        for (int i = 0; i < tempProductions.size(); i++) {
            GrammarProduction gp = tempProductions.get(i);
            if (gp.rightSide().contains(leftSide)) {
                String newRightSide = gp.rightSide().replace(leftSide, "");
                this.modifiedChomskyProductions.set(this.modifiedChomskyProductions.indexOf(gp), new GrammarProduction(gp.leftSide(), newRightSide));
            }
        }
    }

    private void detectOccurrencesAndResolveProductions(String leftSide) {
        List<GrammarProduction> tempProductions = new ArrayList<>(this.modifiedChomskyProductions);

        for (int i = 0; i < tempProductions.size(); i++) {
            GrammarProduction gp = tempProductions.get(i);

            if (gp.rightSide().contains(leftSide) && !gp.leftSide().equals(this.chomskyStartSymbol)) {
                String newRightSide = gp.rightSide().replace(leftSide, "");
                this.modifiedChomskyProductions.add(new GrammarProduction(gp.leftSide(), newRightSide));
            }
        }
    }

    private void resolveNonTerminalsOnRightSide() {
        int v_i = 1;
        this.modifiedChomskyProductions = new ArrayList<>(this.chomskyProductions);

        for (GrammarProduction gp : this.chomskyProductions) {
            String currentRightSide = gp.rightSide();
            int indexOfCurrentProduction = this.modifiedChomskyProductions.indexOf(gp);

            while (this.getAnzNonTerminalsOnRightSide(currentRightSide) > 2) {
                String[] nonTerminals = this.getNonTerminalsFromRightSide(currentRightSide);

                this.modifiedChomskyProductions.add(new GrammarProduction("V_" + v_i, nonTerminals[0] + nonTerminals[1]));
                currentRightSide = currentRightSide.replace(nonTerminals[0] + nonTerminals[1], "V_" + v_i);
                this.chomskyNonTerminals.add("V_" + v_i);
                this.modifiedChomskyProductions.set(indexOfCurrentProduction, new GrammarProduction(gp.leftSide(), currentRightSide));

                v_i += 1;
            }
        }
        this.chomskyProductions = new ArrayList<>(this.modifiedChomskyProductions);
    }

    private int getAnzNonTerminalsOnRightSide(String rightSide) {
        int anzNonTerminals = 0;

        for (int i = 0; i < rightSide.length(); i++) {
            StringBuilder nonTerminalToCheck = new StringBuilder(String.valueOf(rightSide.charAt(i)));

            if (nonTerminalToCheck.toString().equals("Z") || nonTerminalToCheck.toString().equals("V")) {
                do {
                    nonTerminalToCheck.append(rightSide.charAt(i + 1));
                    i += 1;

                    if ((i+1) >= rightSide.length()) {
                        break;
                    }
                } while (Character.isDigit(rightSide.charAt(i+1)) || this.chomskyTerminals.contains(String.valueOf(rightSide.charAt(i+1))));
            }

            if (this.chomskyNonTerminals.stream().anyMatch(nonTerminalToCheck.toString()::contains)) {
                anzNonTerminals++;
            }
        }

        return anzNonTerminals;
    }

    private String[] getNonTerminalsFromRightSide(String rightSide) {
        List<String> nonTerminals = new ArrayList<>();

        for (int i = 0; i < rightSide.length(); i++) {
            StringBuilder nonTerminalToCheck = new StringBuilder(String.valueOf(rightSide.charAt(i)));

            if (nonTerminalToCheck.toString().equals("Z") || nonTerminalToCheck.toString().equals("V")) {
                do {
                    nonTerminalToCheck.append(rightSide.charAt(i + 1));
                    i += 1;

                    if ((i+1) >= rightSide.length()) {
                        break;
                    }
                } while (Character.isDigit(rightSide.charAt(i+1)) || this.chomskyTerminals.contains(String.valueOf(rightSide.charAt(i+1))));
            }

            if (this.chomskyNonTerminals.stream().anyMatch(nonTerminalToCheck.toString()::contains)) {
                nonTerminals.add(nonTerminalToCheck.toString());
            }
        }

        return nonTerminals.toArray(new String[0]);
    }

    private void resolveSingleProductions() {
        this.modifiedChomskyProductions = new ArrayList<>(this.chomskyProductions);

        for (GrammarProduction gp : this.chomskyProductions) {
            if (this.getAnzNonTerminalsOnRightSide(gp.rightSide()) == 1) {
                this.modifiedChomskyProductions.remove(gp);
                this.replaceSingleProductions(gp.leftSide(), gp.rightSide());
            }
        }

        this.chomskyProductions = new ArrayList<>(this.modifiedChomskyProductions);
    }

    private void replaceSingleProductions(String leftSide, String rightSide) {
        List<GrammarProduction> tempList = new ArrayList<>(this.modifiedChomskyProductions);
        for (GrammarProduction gp : tempList) {
            if (gp.leftSide().equals(rightSide) && !this.chomskyNonTerminals.contains(gp.rightSide())) {
                this.modifiedChomskyProductions.add(new GrammarProduction(leftSide, gp.rightSide()));
            }
        }
    }

    private void grammarProductionCleanUp() {
        this.chomskyProductions = this.chomskyProductions.stream().distinct().collect(Collectors.toList());
        this.modifiedChomskyProductions = new ArrayList<>(this.chomskyProductions);

        for (GrammarProduction gp : this.chomskyProductions) {
            if (gp.rightSide().equals("")) {
                this.modifiedChomskyProductions.remove(gp);
            }
        }

        this.chomskyProductions = new ArrayList<>(this.modifiedChomskyProductions);
    }

}
