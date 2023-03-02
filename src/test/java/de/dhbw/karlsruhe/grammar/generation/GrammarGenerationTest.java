package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.Grammar;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GrammarGenerationTest {
    @Test
    public void correctGrammarGenerationTest() throws FileNotFoundException {
        GrammarGeneration grammarGeneration = new GrammarGeneration();
        for (int i = 0; i < 10; i++) {
            Grammar createdGrammar = grammarGeneration.generateGrammar();
            System.out.println();
            assertNotNull(createdGrammar);
        }
    }
}
