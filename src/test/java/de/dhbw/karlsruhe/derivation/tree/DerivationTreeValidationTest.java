package de.dhbw.karlsruhe.derivation.tree;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.dhbw.karlsruhe.derivation.tree.models.DetailResult;
import de.dhbw.karlsruhe.derivation.tree.validation.DerivationTreeValidation;
import de.dhbw.karlsruhe.derivation.tree.validation.SetupValidationTree;
import de.dhbw.karlsruhe.models.GrammarProduction;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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

   assertEquals( new DetailResult(true),
       derivationTreeValidation.checkTree(setupValidationTree.root(), "(())"));
  }

  @Test
   void checkWrongValidationTreeTest() throws FileNotFoundException {
    String grammarAsJson = getGrammarAsJson(
        "src/test/resources/derivation_tree/grammarCorrect1.json");
    String treeAsJson = getTreeAsJson(
        "src/test/resources/derivation_tree/tree1.json");
    SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
    DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
        grammarAsJson);

   assertEquals(new DetailResult(false, "Das abzuleitende Wort ist leer."),
       derivationTreeValidation.checkTree(setupValidationTree.root(), null));
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

   GrammarProduction production = new GrammarProduction("S", "(S)");

   assertEquals(new DetailResult(false, production,
            String.format("Die Produktion %s existiert nicht in der Grammatik.", production)),
        derivationTreeValidation.checkTree(setupValidationTree.root(), null));
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

    assertEquals(new DetailResult(false, "NNZ ist nicht das Startsymbol der Grammatik."),
        derivationTreeValidation.checkTree(setupValidationTree.root(), null));
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

    assertEquals(new DetailResult(false, "3 ist nicht das Startsymbol der Grammatik."), derivationTreeValidation.checkTree(setupValidationTree.root(), null));
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

    assertEquals(new DetailResult(false, new GrammarProduction("3", "NNZ"), "Das Terminal 3 darf keine Kinder haben."),
        derivationTreeValidation.checkTree(setupValidationTree.root(), null));
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

    assertEquals(new DetailResult(false, "Das abgeleitete Wort (()) entspricht nicht dem Ausgangswort ((])"),
        derivationTreeValidation.checkTree(setupValidationTree.root(), "((])"));
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

    assertEquals(new DetailResult(true), derivationTreeValidation.checkTree(setupValidationTree.root(), "302"));
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

    assertEquals(new DetailResult(false, "Das abgeleitete Wort 302 entspricht nicht dem Ausgangswort 145"), derivationTreeValidation.checkTree(setupValidationTree.root(), "145"));
  }

  private String getGrammarAsJson(String path) throws FileNotFoundException {
    return new Scanner(new File(path)).useDelimiter("\\Z").next();
  }

  private String getTreeAsJson(String path) throws FileNotFoundException {
    return new Scanner(new File(path)).useDelimiter("\\Z").next();
  }

}
