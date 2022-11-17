package de.dhbw.karlsruhe;

import de.dhbw.karlsruhe.derivation.tree.validation.DerivationTreeValidation;
import de.dhbw.karlsruhe.derivation.tree.validation.SetupValidationTree;
import de.dhbw.karlsruhe.util.Resource;
import java.io.IOException;

public class App {

  public static void main(String[] args) {

    Resource resource = new Resource();
    String grammarAsJson = "";
    String treeAsJson = "";

    try {
      grammarAsJson = resource.getResourceAsString("examples/derivation_tree/exampleGrammar.json");
      treeAsJson = resource.getResourceAsString(
          "examples/derivation_tree/exampleDerivationTree.json");
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

    if (!grammarAsJson.isBlank() && !treeAsJson.isBlank()) {
      SetupValidationTree setupValidationTree = new SetupValidationTree(treeAsJson);
      DerivationTreeValidation derivationTreeValidation = new DerivationTreeValidation(
          grammarAsJson);
      System.out.println(derivationTreeValidation.checkTree(setupValidationTree.root(), ""));
    } else {
      System.out.println("Json could not be read.");
    }
  }
}
