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
    String grammarAsJson = getGrammarAsJson(
        "src/test/resources/derivation_tree/exampleGrammarCorrect.json");
    String treeAsJson = getTreeAsJson(
        "src/test/resources/derivation_tree/exampleDerivationTreeCorrect.json");
    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    assertTrue(derivationTreeValidation.checkTree(setupValidationTree.root()));
  }

  @Test
  public void checkWrongValidationTreeTest() throws FileNotFoundException {
    String grammarAsJson = getGrammarAsJson(
        "src/test/resources/derivation_tree/exampleGrammarCorrect.json");
    String treeAsJson = getTreeAsJson(
        "src/test/resources/derivation_tree/exampleDerivationTreeWrong.json");
    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    assertFalse(derivationTreeValidation.checkTree(setupValidationTree.root()));
  }

  @Test
  public void checkWrongGrammarTest() throws FileNotFoundException {
    String grammarAsJson = getGrammarAsJson(
        "src/test/resources/derivation_tree/exampleGrammarWrong.json");
    String treeAsJson = getTreeAsJson(
        "src/test/resources/derivation_tree/exampleDerivationTreeCorrect.json");
    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    assertFalse(derivationTreeValidation.checkTree(setupValidationTree.root()));
  }

  private String getGrammarAsJson(String path) throws FileNotFoundException {
    return new Scanner(new File(path)).useDelimiter("\\Z").next();
  }

  private String getTreeAsJson(String path) throws FileNotFoundException {
    return new Scanner(new File(path)).useDelimiter("\\Z").next();
  }

}
