package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.services.ProductionService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GrammarConcatenationGenerationTest {
    @Test
    public void correctGrammarGenerationTest() throws FileNotFoundException {
        GrammarConcatenationGeneration grammarConcatenationGeneration = new GrammarConcatenationGeneration();
        for (int i = 0; i < 100; i++) {
            Grammar createdGrammar = grammarConcatenationGeneration.generateGrammar();
            assertNotNull(createdGrammar);
        }
    }

    @Test
    public void checkEmptyIntersectionBetweenTerminalsAndNonTerminals() {
        GrammarGeneration grammarGeneration = new GrammarConcatenationGeneration();
        Grammar generatedGrammar = grammarGeneration.generateGrammar();

        assertTrue(Arrays.stream(generatedGrammar.getTerminals()).distinct()
                .filter(x -> Arrays.asList(generatedGrammar.getNonTerminals()).contains(x)).toList()
                .isEmpty());
    }

    @Test
    public void checkThatGrammarGenerationsAreNotEmpty() {
        GrammarGeneration grammarGeneration = new GrammarConcatenationGeneration();
        Grammar generatedGrammar = grammarGeneration.generateGrammar();

        assertNotEquals(0, generatedGrammar.getTerminals().length);
        assertNotEquals(0, generatedGrammar.getNonTerminals().length);
        assertNotEquals(0, generatedGrammar.getProductions().length);
        assertFalse(generatedGrammar.getStartSymbol().isBlank());
    }

    @Test
    public void checkThatStartSymbolIsNonTerminal() {
        GrammarGeneration grammarGeneration = new GrammarConcatenationGeneration();
        Grammar generatedGrammar = grammarGeneration.generateGrammar();

        assertTrue(
                Arrays.asList(generatedGrammar.getNonTerminals()).contains(generatedGrammar.getStartSymbol()));
    }

    @Test
    void checkAllNonTerminalsOnLeftSide(){
        GrammarGeneration grammarGeneration = new GrammarConcatenationGeneration();
        Grammar generatedGrammar = grammarGeneration.generateGrammar();

        List<GrammarProduction> grList = new ArrayList<>(Arrays.stream(generatedGrammar.getProductions()).toList());
        for (String nonTerminal : generatedGrammar.getNonTerminals()) {
            assertTrue(grList.stream().anyMatch(production -> StringUtils.startsWith(production.leftSide(), nonTerminal)));
        }
    }

    @Test
    void checkAllTerminalsUsed(){
        GrammarGeneration grammarGeneration = new GrammarConcatenationGeneration();
        Grammar generatedGrammar = grammarGeneration.generateGrammar();

        List<GrammarProduction> grList = new ArrayList<>(Arrays.stream(generatedGrammar.getProductions()).toList());
        for (String terminal : generatedGrammar.getTerminals()) {
            assertTrue(grList.stream().anyMatch(production -> StringUtils.contains(production.rightSide(), terminal) || production.rightSide().equals("epsilon")));
        }
    }

    @Test
    void checkAtLeastOneEndproduction() {
        ProductionService productionService = new ProductionService();
        for (int i =0; i<1000; i++) {
            GrammarGeneration grammarGeneration = new GrammarConcatenationGeneration();
            Grammar generatedGrammar = grammarGeneration.generateGrammar();
            List<GrammarProduction> grList = new ArrayList<>(Arrays.stream(generatedGrammar.getProductions()).toList());
            assertTrue(grList.stream().anyMatch(productionService::isEndProduction));
        }
    }
}
