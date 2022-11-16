package de.dhbw.karlsruhe.derivation.tree;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.dhbw.karlsruhe.derivation.tree.validation.DerivationTreeValidation;
import de.dhbw.karlsruhe.derivation.tree.validation.SetupValidationTree;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.junit.Test;

public class DerivationTreeValidationTest {

  @Test
  public void checkCorrectValidationTreeTest() throws FileNotFoundException {
    String grammarAsJson = new Scanner(
        new File("src/test/resources/test_examples/exampleGrammarCorrect.json")).useDelimiter("\\Z")
        .next();
    String treeAsJson = new Scanner(
        new File(
            "src/test/resources/test_examples/exampleDerivationTreeCorrect.json")).useDelimiter(
            "\\Z")
        .next();

    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    assertTrue(derivationTreeValidation.checkTree(setupValidationTree.root()));
  }

  @Test
  public void checkWrongValidationTreeTest() throws FileNotFoundException {
    String grammarAsJson = new Scanner(
        new File("src/test/resources/test_examples/exampleGrammarCorrect.json")).useDelimiter("\\Z")
        .next();
    String treeAsJson = new Scanner(
        new File(
            "src/test/resources/test_examples/exampleDerivationTreeWrong.json")).useDelimiter(
            "\\Z")
        .next();

    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    assertFalse(derivationTreeValidation.checkTree(setupValidationTree.root()));
  }

  @Test
  public void checkWrongGrammarTest() throws FileNotFoundException {
    String grammarAsJson = new Scanner(
        new File("src/test/resources/test_examples/exampleGrammarWrong.json")).useDelimiter("\\Z")
        .next();
    String treeAsJson = new Scanner(
        new File(
            "src/test/resources/test_examples/exampleDerivationTreeCorrect.json")).useDelimiter(
            "\\Z")
        .next();

    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    assertFalse(derivationTreeValidation.checkTree(setupValidationTree.root()));
  }

}
