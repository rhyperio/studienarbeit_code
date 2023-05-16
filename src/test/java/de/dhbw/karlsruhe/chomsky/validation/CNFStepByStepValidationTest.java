package de.dhbw.karlsruhe.chomsky.validation;

import de.dhbw.karlsruhe.chomsky.generation.ChomskyTransformationGeneration;
import de.dhbw.karlsruhe.grammar.generation.GrammarConcatenationGeneration;
import de.dhbw.karlsruhe.grammar.generation.GrammarGeneration;
import de.dhbw.karlsruhe.models.Grammar;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CNFStepByStepValidationTest {
    @Test
    public void checkStepByStepValidation() {
        ChomskyTransformationGeneration chomskyTransformationGeneration = new ChomskyTransformationGeneration();
        CNFStepByStepValidation cnfStepByStepValidation = new CNFStepByStepValidation();
        GrammarGeneration grammarGeneration = new GrammarConcatenationGeneration();

        for (int i = 0; i < 10; i++) {
            Grammar grammarToTransform = grammarGeneration.generateGrammar();
            Grammar[] transformedGrammarStepByStep = chomskyTransformationGeneration.getStepByStepTransformedCNFGrammar(grammarToTransform);

            assertTrue(cnfStepByStepValidation.validateGrammarIsInCorrectCNF(grammarToTransform, transformedGrammarStepByStep));
        }
    }
}
