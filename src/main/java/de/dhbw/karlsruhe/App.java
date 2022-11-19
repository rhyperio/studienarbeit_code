package de.dhbw.karlsruhe;

import com.google.gson.Gson;
import de.dhbw.karlsruhe.bottom.up.models.BottomUpAcceptor;
import de.dhbw.karlsruhe.bottom.up.models.BottomUpState;
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

    /*
    Test for BUAcceptor Creation

    GrammarRule gREpsilon = new GrammarRule("S", "epsilon");
    GrammarRule gRBrackets = new GrammarRule("S", "( S )");
    GrammarRule gRDouble = new GrammarRule("S", "S S");

    BottomUpAcceptor buAcceptor = new BottomUpAcceptor();
    buAcceptor.addStep("*", BottomUpState.z0, "(())", null);
    buAcceptor.addStep("*(", BottomUpState.z, "())", null);
    buAcceptor.addStep("*((", BottomUpState.z, "))", null);
    buAcceptor.addStep("*((S", BottomUpState.z, "))", gREpsilon);
    buAcceptor.addStep("*((S)", BottomUpState.z, ")", null);
    buAcceptor.addStep("*(S", BottomUpState.z, ")", gRBrackets);
    buAcceptor.addStep("*(S)", BottomUpState.z, "", null);
    buAcceptor.addStep("*S", BottomUpState.z, "", gRBrackets);
    buAcceptor.addStep("*S", BottomUpState.zf, "", null);

    Gson gson = new Gson();
    String bUAcceptorJson = gson.toJson(buAcceptor);
    System.out.printf(bUAcceptorJson);
     */


  }

}
