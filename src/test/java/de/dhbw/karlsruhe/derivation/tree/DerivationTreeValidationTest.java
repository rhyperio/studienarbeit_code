package de.dhbw.karlsruhe.derivation.tree;

import static org.junit.jupiter.api.Assertions.assertTrue;

import de.dhbw.karlsruhe.derivation.tree.validation.DerivationTreeValidation;
import de.dhbw.karlsruhe.derivation.tree.validation.SetupValidationTree;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

 class DerivationTreeValidationTest {

  @Test
   void checkCorrectValidationTreeTest() throws FileNotFoundException {
    String grammarAsJson = getGrammarAsJson(
        "src/test/resources/derivation_tree/grammarCorrect1.json");
    String treeAsJson = getTreeAsJson(
        "src/test/resources/derivation_tree/tree1.json");
    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    assertTrue(derivationTreeValidation.checkTree(setupValidationTree.root(), "(())"));
  }

  @Test
   void checkWrongValidationTreeTest() throws FileNotFoundException {
    String grammarAsJson = getGrammarAsJson(
        "src/test/resources/derivation_tree/grammarCorrect1.json");
    String treeAsJson = getTreeAsJson(
        "src/test/resources/derivation_tree/treeWithWrongTerminal1.json");
    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    Assertions.assertFalse(derivationTreeValidation.checkTree(setupValidationTree.root(), null));
  }

  @Test
   void checkWrongGrammarTest() throws FileNotFoundException {
    String grammarAsJson = getGrammarAsJson(
        "src/test/resources/derivation_tree/grammarWithWrongTerminalInProduction1.json");
    String treeAsJson = getTreeAsJson(
        "src/test/resources/derivation_tree/tree1.json");
    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    Assertions.assertFalse(derivationTreeValidation.checkTree(setupValidationTree.root(), null));
  }

  @Test
   void checkWrongStartTerminalInTree() throws FileNotFoundException {
    String grammarAsJson = getGrammarAsJson(
        "src/test/resources/derivation_tree/grammarCorrect2.json");
    String treeAsJson = getTreeAsJson(
        "src/test/resources/derivation_tree/treeWithWrongStartNonTerminal2.json");
    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    Assertions.assertFalse(derivationTreeValidation.checkTree(setupValidationTree.root(), null));
  }

  @Test
   void checkTerminalAsStartSymbolInTree() throws FileNotFoundException {
    String grammarAsJson = getGrammarAsJson(
        "src/test/resources/derivation_tree/grammarCorrect2.json");
    String treeAsJson = getTreeAsJson(
        "src/test/resources/derivation_tree/treeWithTerminalAsStartSymbol2.json");
    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    Assertions.assertFalse(derivationTreeValidation.checkTree(setupValidationTree.root(), null));
  }

  @Test
   void checkTreeWithProductionFromTerminalToNonTerminal() throws FileNotFoundException {
    String grammarAsJson = getGrammarAsJson(
        "src/test/resources/derivation_tree/grammarCorrect3.json");
    String treeAsJson = getTreeAsJson(
        "src/test/resources/derivation_tree/treeWithTerminalToNonTerminal3.json");
    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    Assertions.assertFalse(derivationTreeValidation.checkTree(setupValidationTree.root(), null));
  }

  @Test
   void isWrongWordInDerivationTree() throws FileNotFoundException {
    String grammarAsJson = getGrammarAsJson(
        "src/test/resources/derivation_tree/grammarCorrect1.json");
    String treeAsJson = getTreeAsJson(
        "src/test/resources/derivation_tree/tree1.json");
    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    Assertions.assertFalse(derivationTreeValidation.checkTree(setupValidationTree.root(), "((])"));
  }

  @Test
   void isCorrectNumberWordInDerivationTree() throws FileNotFoundException {
    String grammarAsJson = getGrammarAsJson(
        "src/test/resources/derivation_tree/grammarCorrect2.json");
    String treeAsJson = getTreeAsJson(
        "src/test/resources/derivation_tree/tree2.json");
    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    assertTrue(derivationTreeValidation.checkTree(setupValidationTree.root(), "302"));
  }

  @Test
   void isWrongNumberWordInDerivationTree() throws FileNotFoundException {
    String grammarAsJson = getGrammarAsJson(
        "src/test/resources/derivation_tree/grammarCorrect2.json");
    String treeAsJson = getTreeAsJson(
        "src/test/resources/derivation_tree/tree2.json");
    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

    Assertions.assertFalse(derivationTreeValidation.checkTree(setupValidationTree.root(), "145"));
  }

  private String getGrammarAsJson(String path) throws FileNotFoundException {
    return new Scanner(new File(path)).useDelimiter("\\Z").next();
  }

  private String getTreeAsJson(String path) throws FileNotFoundException {
    return new Scanner(new File(path)).useDelimiter("\\Z").next();
  }

}
