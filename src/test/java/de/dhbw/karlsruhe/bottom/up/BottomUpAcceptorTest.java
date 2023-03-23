package de.dhbw.karlsruhe.bottom.up;

import com.google.gson.Gson;
import de.dhbw.karlsruhe.bottom.up.models.AcceptorDetailResult;
import de.dhbw.karlsruhe.bottom.up.models.BottomUpAcceptor;
import de.dhbw.karlsruhe.bottom.up.validation.BottomUpAcceptorValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class BottomUpAcceptorTest {

    @Test
     void checkCorrectBottomUpAcceptorValidationTest() throws FileNotFoundException {
        String grammarAsJson = getGrammarAsJson(
                "src/test/resources/derivation_tree/grammarCorrect1.json");
        String bottomUpAcceptorAsJson = getBottomUpAcceptorAsJson(
                "src/test/resources/bottom_up/bUAcceptorCorrect1.json");
        Gson gson = new Gson();

        BottomUpAcceptor buAcceptor = gson.fromJson(bottomUpAcceptorAsJson, BottomUpAcceptor.class);
        BottomUpAcceptorValidation bUAcceptorValidation = new BottomUpAcceptorValidation(grammarAsJson);
        String word = "(())";
        assertTrue(bUAcceptorValidation.checkAcceptor(buAcceptor, word).isCorrect());
    }


    @Test
     void checkNullWordBottomUpAcceptorValidationTest() throws IOException {
        String grammarAsJson = getGrammarAsJson(
                "src/test/resources/derivation_tree/grammarCorrect1.json");
        String bottomUpAcceptorAsJson = getBottomUpAcceptorAsJson(
                "src/test/resources/bottom_up/bUAcceptorCorrect1.json");
        Gson gson = new Gson();

        BottomUpAcceptor buAcceptor = gson.fromJson(bottomUpAcceptorAsJson, BottomUpAcceptor.class);
        BottomUpAcceptorValidation bUAcceptorValidation = new BottomUpAcceptorValidation(grammarAsJson);
        assertEquals(new AcceptorDetailResult(false,"Es sind nicht alle Parameter gegeben."),
                bUAcceptorValidation.checkAcceptor(buAcceptor, null));
    }

     @Test
     void checkNullBottomUpAcceptorValidationTest() throws FileNotFoundException {
         String grammarAsJson = getGrammarAsJson(
                 "src/test/resources/derivation_tree/grammarCorrect1.json");

         BottomUpAcceptorValidation bUAcceptorValidation = new BottomUpAcceptorValidation(grammarAsJson);
         String word = "(())";
         assertEquals(new AcceptorDetailResult(false,"Es sind nicht alle Parameter gegeben."),
                 bUAcceptorValidation.checkAcceptor(null, word));
     }

     @ParameterizedTest
     @ValueSource(strings = {"src/test/resources/bottom_up/bUAcceptorWrongProduction1.json",
             "src/test/resources/bottom_up/bUAcceptorWrongProduction2.json",
             "src/test/resources/bottom_up/bUAcceptorWrongMissingProduction3.json",
             "src/test/resources/bottom_up/bUAcceptorWrongMissingReading1.json",
             "src/test/resources/bottom_up/bUAcceptorWrongReading2.json",
             "src/test/resources/bottom_up/bUAcceptorWrongMissingPriorToLastStep.json",
             "src/test/resources/bottom_up/bUAcceptorWrongMissingProduction4.json",
             "src/test/resources/bottom_up/bUAcceptorWrongFirstStepState.json",
             "src/test/resources/bottom_up/bUAcceptorWrongFirstStepHasProduction.json",
             "src/test/resources/bottom_up/bUAcceptorWrongFirstStepStack.json",
             "src/test/resources/bottom_up/bUAcceptorWrongFirstStepRemainingWord.json",
             "src/test/resources/bottom_up/bUAcceptorWrongLastStepState.json",
             "src/test/resources/bottom_up/bUAcceptorWrongLastStepStack.json",
             "src/test/resources/bottom_up/bUAcceptorWrongLastStepRemainingWord.json",
             "src/test/resources/bottom_up/bUAcceptorWrongLastStepHasProduction.json"
            })
     void checkWrongBottomUpAcceptorValidationTest(String buAcceptorPath) throws FileNotFoundException {
         String grammarAsJson = getGrammarAsJson(
                 "src/test/resources/derivation_tree/grammarCorrect1.json");
         String bottomUpAcceptorAsJson = getBottomUpAcceptorAsJson(buAcceptorPath);
         Gson gson = new Gson();

         BottomUpAcceptor buAcceptor = gson.fromJson(bottomUpAcceptorAsJson, BottomUpAcceptor.class);
         BottomUpAcceptorValidation bUAcceptorValidation = new BottomUpAcceptorValidation(grammarAsJson);
         String word = "(())";
         assertFalse(bUAcceptorValidation.checkAcceptor(buAcceptor, word).isCorrect());
     }

     @ParameterizedTest
     @ValueSource(strings = {"((", "))", " ", "(()))" })
     void checkWrongWordForBottomUpAcceptorValidationTest(String word) throws FileNotFoundException {
         String grammarAsJson = getGrammarAsJson(
                 "src/test/resources/derivation_tree/grammarCorrect1.json");
         String bottomUpAcceptorAsJson = getBottomUpAcceptorAsJson(
                 "src/test/resources/bottom_up/bUAcceptorCorrect1.json");
         Gson gson = new Gson();

         BottomUpAcceptor buAcceptor = gson.fromJson(bottomUpAcceptorAsJson, BottomUpAcceptor.class);
         BottomUpAcceptorValidation bUAcceptorValidation = new BottomUpAcceptorValidation(grammarAsJson);
         assertFalse(bUAcceptorValidation.checkAcceptor(buAcceptor, word).isCorrect());
     }

     @ParameterizedTest
     @ValueSource(strings = {"src/test/resources/derivation_tree/grammarCorrect2.json",
             "src/test/resources/derivation_tree/grammarWithWrongTerminalInProduction1.json"
            })
     void checkWrongGrammarForBottomUpAcceptorAndWordTest(String grammarPath) throws FileNotFoundException {
         String grammarAsJson = getGrammarAsJson(grammarPath);
         String bottomUpAcceptorAsJson = getBottomUpAcceptorAsJson(
                 "src/test/resources/bottom_up/bUAcceptorCorrect1.json");
         Gson gson = new Gson();

         BottomUpAcceptor buAcceptor = gson.fromJson(bottomUpAcceptorAsJson, BottomUpAcceptor.class);
         BottomUpAcceptorValidation bUAcceptorValidation = new BottomUpAcceptorValidation(grammarAsJson);
         String word = "(())";
         assertFalse(bUAcceptorValidation.checkAcceptor(buAcceptor, word).isCorrect());
     }


    private String getGrammarAsJson(String path) throws FileNotFoundException {
    return new Scanner(new File(path)).useDelimiter("\\Z").next();
  }

    private String getBottomUpAcceptorAsJson(String path) throws FileNotFoundException {
        return new Scanner(new File(path)).useDelimiter("\\Z").next();
    }

}

