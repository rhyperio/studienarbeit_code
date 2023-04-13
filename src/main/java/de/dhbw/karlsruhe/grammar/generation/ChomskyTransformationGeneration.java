package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;

import java.util.*;

public class ChomskyTransformationGeneration {
    private Grammar typeTwoGrammar;
    private List<GrammarProduction> chomskyProductions;
    private Set<String> chomskyNonTerminals;
    private String chomskyStartSymbol;

    public ChomskyTransformationGeneration() {
    }

    public Grammar GenerateChomskyGrammar() {
        do {
            this.typeTwoGrammar = new GrammarConcatenationGeneration().generateGrammar();
        } while (Arrays.stream(this.typeTwoGrammar.getNonTerminals()).toList().contains("Z") || Arrays.stream(this.typeTwoGrammar.getNonTerminals()).toList().contains("V"));
        this.chomskyProductions = new ArrayList<>(List.of(this.typeTwoGrammar.getProductions()));
        this.chomskyNonTerminals = new HashSet<>(List.of(this.typeTwoGrammar.getNonTerminals()));
        this.chomskyStartSymbol = this.typeTwoGrammar.getStartSymbol();

        Set<String> epsG = this.getEPSFromGrammar();
        boolean epsIsInLanguage = epsG.contains(this.typeTwoGrammar.getStartSymbol());

        this.replaceTerminals();
        this.resolveEpsilonProduction(epsIsInLanguage);
        this.resolveNonTerminalsOnRightSide();
        this.resolveSingleProductions();

        return new Grammar(this.typeTwoGrammar.getTerminals(), this.chomskyNonTerminals.toArray(new String[0]), this.chomskyProductions.toArray(new GrammarProduction[0]), this.chomskyStartSymbol);
    }

    private Set<String> getEPSFromGrammar() {
        Set<String> m_current = new HashSet<>();
        Set<String> m_new = new HashSet<>();

        for (GrammarProduction gp : typeTwoGrammar.getProductions()) {
            if (gp.rightSide().equals("ε")) {
                m_current.add(gp.leftSide());
            }
        }

        do {
            m_new = new HashSet<>(m_current);

            for (GrammarProduction gp : typeTwoGrammar.getProductions()) {
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
        for (GrammarProduction gp : this.chomskyProductions) {
            if (gp.rightSide().equals("ε") && !gp.leftSide().equals("S'")) {
                this.chomskyProductions.remove(gp);
                int anzProductions = this.getAnzProductionsOfLeftNonTerminal(gp.leftSide());

                if (anzProductions == 0) {
                    this.removeOccurenciesOfNonTerminal(gp.leftSide());
                } else {
                    this.detectOccurrencesAndResolveProductions(gp.leftSide());
                }
            }
        }
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
        for (GrammarProduction gp : this.chomskyProductions) {
            if (gp.rightSide().contains(leftSide)) {
                String newRightSide = gp.rightSide().replace(leftSide, "");
                this.chomskyProductions.set(this.chomskyProductions.indexOf(gp), new GrammarProduction(gp.leftSide(), newRightSide));
            }
        }
    }

    private void detectOccurrencesAndResolveProductions(String leftSide) {
        for (GrammarProduction gp : this.chomskyProductions) {
            if (gp.rightSide().contains(leftSide)) {
                String newRightSide = gp.rightSide().replace(leftSide, "");
                this.chomskyProductions.add(new GrammarProduction(gp.leftSide(), newRightSide));
            }
        }
    }

    private void resolveNonTerminalsOnRightSide() {
        String leftToProof;
        String rightToProof;
        int v_i = 0;

        for (GrammarProduction gp : this.chomskyProductions) {
            for (int i = 0; i < gp.rightSide().length(); i++) {
                leftToProof = String.valueOf(gp.rightSide().charAt(i));

                if (leftToProof.equals("Z") || leftToProof.equals("V")) {
                    leftToProof += gp.rightSide().charAt(i+1);
                    leftToProof += gp.rightSide().charAt(i+2);
                    i += 2;
                }

                rightToProof = String.valueOf(gp.rightSide().charAt(i+1));

                if (rightToProof.equals("Z") || rightToProof.equals("V")) {
                    rightToProof += gp.rightSide().charAt(i+1);
                    rightToProof += gp.rightSide().charAt(i+2);
                    i += 2;
                }

                
            }
        }
    }

    private void resolveSingleProductions() {
        Set[] derivateableNonTerminals = new Set[this.chomskyNonTerminals.size()];
        Set<String> m_current = new HashSet<>();
        Set<String> m_new;
        int i = 0;

        // get non Terminals which are leading to only one non Terminal
        for (String nonTerminal : this.chomskyNonTerminals) {
            m_current.add(nonTerminal);
            m_new = new HashSet<>(m_current);

            do {
                m_current = m_new;

                for (GrammarProduction gp : this.chomskyProductions) {
                    if (gp.leftSide().equals(nonTerminal) && (gp.rightSide().length() == 1) && this.chomskyNonTerminals.contains(gp.rightSide())) {
                        m_new.add(gp.rightSide());
                    }
                }

            } while (!m_current.equals(m_new));

            derivateableNonTerminals[i] = m_new;
            i++;
        }

        // remove productions to only one non terminal like X -> X'
        for (GrammarProduction gp : this.chomskyProductions) {
            if (gp.rightSide().length() == 1 && this.chomskyNonTerminals.contains(gp.rightSide())) {
                this.chomskyProductions.remove(gp);
            }
        }

        // add prodcution replacement production


    }

}
