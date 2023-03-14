package de.dhbw.karlsruhe.grammar.generation;

import de.dhbw.karlsruhe.models.Grammar;
import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GrammarConcatenationGenerationTest {
    @Test
    public void correctGrammarGenerationTest() throws FileNotFoundException {
        GrammarConcatenationGeneration grammarConcatenationGeneration = new GrammarConcatenationGeneration();
        for (int i = 0; i < 100; i++) {
            Grammar createdGrammar = grammarConcatenationGeneration.generateGrammar();
            assertNotNull(createdGrammar);
        }
    }
}
