package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ChomskyTransformationGeneration {
    private Grammar typeTwoGrammar;
    private Set<GrammarProduction> chomskyRulesSet;

    public ChomskyTransformationGeneration() {
    }

    public Grammar GenerateChomskyGrammar() {
        this.typeTwoGrammar = new GrammarConcatenationGeneration().generateGrammar();

        String[] epsG = this.getEPSFromGrammar();

        return new Grammar();
    }

    private String[] getEPSFromGrammar() {
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

        return m_new.toArray(new String[0]);
    }

}
