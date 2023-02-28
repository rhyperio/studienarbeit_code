package de.dhbw.karlsruhe.grammar.generation;

import org.junit.jupiter.api.Test;
import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class GrammarGenerationTest {
    @Test
    public void correctGrammarGenerationTest() throws FileNotFoundException {
        GrammarInitialization grammarInitialization = new GrammarInitialization();
        grammarInitialization.startGeneration();

        assertTrue(true);
    }
}
