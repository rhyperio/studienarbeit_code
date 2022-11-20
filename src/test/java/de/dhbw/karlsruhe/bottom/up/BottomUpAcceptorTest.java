package de.dhbw.karlsruhe.bottom.up;

import com.google.gson.Gson;
import de.dhbw.karlsruhe.bottom.up.models.BottomUpAcceptor;
import de.dhbw.karlsruhe.bottom.up.validation.BottomUpAcceptorValidation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

 class BottomUpAcceptorTest {

    @Test
     void checkCorrectBottomUpAcceptorValidationTest() throws FileNotFoundException {
        String grammarAsJson = getGrammarAsJson(
                "src/test/resources/derivation_tree/grammarCorrect1.json");

        Gson gson = new Gson();

        try (Reader reader = new FileReader(
                "src/test/resources/bottom_up/bUAcceptorCorrect1.json")) {
            BottomUpAcceptor buAcceptor = gson.fromJson(reader, BottomUpAcceptor.class);
            BottomUpAcceptorValidation bUAcceptorValidation = new BottomUpAcceptorValidation(grammarAsJson);
            String word = "(())";
            assertTrue(bUAcceptorValidation.checkAcceptor(buAcceptor, word));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
     void checkNullWordBottomUpAcceptorValidationTest() throws IOException {
        String grammarAsJson = getGrammarAsJson(
                "src/test/resources/derivation_tree/grammarCorrect1.json");

        Gson gson = new Gson();

        try (Reader reader = new FileReader(
                "src/test/resources/bottom_up/bUAcceptorCorrect1.json")) {
            BottomUpAcceptor buAcceptor = gson.fromJson(reader, BottomUpAcceptor.class);
            BottomUpAcceptorValidation bUAcceptorValidation = new BottomUpAcceptorValidation(grammarAsJson);
            assertFalse(bUAcceptorValidation.checkAcceptor(buAcceptor, null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     @Test
     void checkNullBottomUpAcceptorValidationTest() throws FileNotFoundException {
         String grammarAsJson = getGrammarAsJson(
                 "src/test/resources/derivation_tree/grammarCorrect1.json");

         BottomUpAcceptorValidation bUAcceptorValidation = new BottomUpAcceptorValidation(grammarAsJson);
         String word = "(())";
         assertFalse(bUAcceptorValidation.checkAcceptor(null, word));
     }

     @ParameterizedTest
     @ValueSource(strings = {"src/test/resources/bottom_up/bUAcceptorWrongProduction1.json",
             "src/test/resources/bottom_up/bUAcceptorWrongProduction2.json",
             "src/test/resources/bottom_up/bUAcceptorWrongMissingProduction3.json",
             "src/test/resources/bottom_up/bUAcceptorWrongMissingReading1.json",
             "src/test/resources/bottom_up/bUAcceptorWrongReading2.json",
             "src/test/resources/bottom_up/bUAcceptorWrongMissingPriorToLastStep.json"
            })
     void checkWrongBottomUpAcceptorValidationTest(String buAcceptorPath) throws FileNotFoundException {
         String grammarAsJson = getGrammarAsJson(
                 "src/test/resources/derivation_tree/grammarCorrect1.json");
         Gson gson = new Gson();

         try (Reader reader = new FileReader(buAcceptorPath)) {
             BottomUpAcceptor buAcceptor = gson.fromJson(reader, BottomUpAcceptor.class);
             BottomUpAcceptorValidation bUAcceptorValidation = new BottomUpAcceptorValidation(grammarAsJson);
             String word = "(())";
             assertFalse(bUAcceptorValidation.checkAcceptor(buAcceptor, word));
         } catch (IOException e) {
             e.printStackTrace();
         }
     }

     @ParameterizedTest
     @ValueSource(strings = {"((", "))", " ", "(()))" })
     void checkWrongWordForBottomUpAcceptorValidationTest(String word) throws FileNotFoundException {
         String grammarAsJson = getGrammarAsJson(
                 "src/test/resources/derivation_tree/grammarCorrect1.json");
         Gson gson = new Gson();
         try (Reader reader = new FileReader(
                 "src/test/resources/bottom_up/bUAcceptorCorrect1.json")) {
             BottomUpAcceptor buAcceptor = gson.fromJson(reader, BottomUpAcceptor.class);
             BottomUpAcceptorValidation bUAcceptorValidation = new BottomUpAcceptorValidation(grammarAsJson);
             assertFalse(bUAcceptorValidation.checkAcceptor(buAcceptor, word));
         } catch (IOException e) {
             e.printStackTrace();
         }
     }


    private String getGrammarAsJson(String path) throws FileNotFoundException {
    return new Scanner(new File(path)).useDelimiter("\\Z").next();
  }

}

