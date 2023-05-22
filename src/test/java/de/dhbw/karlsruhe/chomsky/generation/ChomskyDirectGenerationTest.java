package de.dhbw.karlsruhe.chomsky.generation;

import de.dhbw.karlsruhe.chomsky.validation.ValidateGrammarCNF;
import de.dhbw.karlsruhe.models.Grammar;
import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.services.ProductionService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChomskyDirectGenerationTest {
    @Test
    public void correctGrammarGenerationTest() {
        ChomskyDirectGeneration chomskyDirectGeneration = new ChomskyDirectGeneration();
        for (int i = 0; i < 100; i++) {
            Grammar generatedGrammar = chomskyDirectGeneration.generateDirectChomskyGrammar();
            assertNotNull(generatedGrammar);
        }
    }

    @Test
    public void checkSynthesisOfChomskyGenerationAndValidation() {
        ChomskyDirectGeneration chomskyDirectGeneration = new ChomskyDirectGeneration();
        ValidateGrammarCNF validateGrammarCNF = new ValidateGrammarCNF();

        for (int i = 0; i < 100; i++) {
            Grammar generatedGrammar = chomskyDirectGeneration.generateDirectChomskyGrammar();
            assertTrue(validateGrammarCNF.validateGrammarIsInCNF(generatedGrammar));
        }
    }

    @Test
    public void checkEmptyIntersectionBetweenTerminalsAndNonTerminals() {
        ChomskyDirectGeneration chomskyDirectGeneration = new ChomskyDirectGeneration();
        Grammar generatedGrammar = chomskyDirectGeneration.generateDirectChomskyGrammar();

        assertTrue(Arrays.stream(generatedGrammar.getTerminals()).distinct()
                .filter(x -> Arrays.asList(generatedGrammar.getNonTerminals()).contains(x)).toList()
                .isEmpty());
    }

    @Test
    public void checkThatGrammarGenerationsAreNotEmpty() {
        ChomskyDirectGeneration chomskyDirectGeneration = new ChomskyDirectGeneration();
        Grammar generatedGrammar = chomskyDirectGeneration.generateDirectChomskyGrammar();

        assertNotEquals(0, generatedGrammar.getTerminals().length);
        assertNotEquals(0, generatedGrammar.getNonTerminals().length);
        assertNotEquals(0, generatedGrammar.getProductions().length);
        assertFalse(generatedGrammar.getStartSymbol().isBlank());
    }

    @Test
    public void checkThatStartSymbolIsNonTerminal() {
        ChomskyDirectGeneration chomskyDirectGeneration = new ChomskyDirectGeneration();

        for (int i =0; i<100; i++) {
            Grammar generatedGrammar = chomskyDirectGeneration.generateDirectChomskyGrammar();

            assertTrue(Arrays.asList(generatedGrammar.getNonTerminals()).contains(generatedGrammar.getStartSymbol()));
        }
    }

    @Test
    void checkAllNonTerminalsOnLeftSide(){
        ChomskyDirectGeneration chomskyDirectGeneration = new ChomskyDirectGeneration();

        for (int i =0; i<100; i++) {
            Grammar generatedGrammar = chomskyDirectGeneration.generateDirectChomskyGrammar();

            List<GrammarProduction> grList = new ArrayList<>(Arrays.stream(generatedGrammar.getProductions()).toList());
            for (String nonTerminal : generatedGrammar.getNonTerminals()) {
                assertTrue(grList.stream().anyMatch(production -> StringUtils.startsWith(production.leftSide(), nonTerminal)));
            }
        }
    }

    @Test
    void checkAllTerminalsUsed(){
        ChomskyDirectGeneration chomskyDirectGeneration = new ChomskyDirectGeneration();

        for (int i =0; i<100; i++) {
            Grammar generatedGrammar = chomskyDirectGeneration.generateDirectChomskyGrammar();

            List<GrammarProduction> grList = new ArrayList<>(Arrays.stream(generatedGrammar.getProductions()).toList());
            for (String terminal : generatedGrammar.getTerminals()) {
                assertTrue(grList.stream().anyMatch(production -> StringUtils.contains(production.rightSide(), terminal) || production.rightSide().equals("epsilon")));
            }
        }
    }

    @Test
    void checkAtLeastOneEndproduction() {
        ProductionService productionService = new ProductionService();
        for (int i =0; i<100; i++) {
            ChomskyDirectGeneration chomskyDirectGeneration = new ChomskyDirectGeneration();
            Grammar generatedGrammar = chomskyDirectGeneration.generateDirectChomskyGrammar();
            List<GrammarProduction> grList = new ArrayList<>(Arrays.stream(generatedGrammar.getProductions()).toList());
            assertTrue(grList.stream().anyMatch(productionService::isEndProduction));
        }
    }
}
