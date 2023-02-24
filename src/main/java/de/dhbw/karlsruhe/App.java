package de.dhbw.karlsruhe;

import com.google.gson.Gson;
import de.dhbw.karlsruhe.bottom.up.models.BottomUpAcceptor;
import de.dhbw.karlsruhe.derivation.tree.validation.DerivationTreeValidation;
import de.dhbw.karlsruhe.derivation.tree.validation.SetupValidationTree;
import de.dhbw.karlsruhe.models.GrammarRule;
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


    // Test for BUAcceptor Creation

    GrammarRule gREpsilon = new GrammarRule("S", "epsilon");
    GrammarRule gRBrackets = new GrammarRule("S", "( S )");
    GrammarRule gRDouble = new GrammarRule("S", "S S");

    BottomUpAcceptor buAcceptor = new BottomUpAcceptor();
    buAcceptor.addStep("*", ParserState.z0, "(())", null);
    buAcceptor.addStep("*(", ParserState.z, "())", null);
    buAcceptor.addStep("*((", ParserState.z, "))", null);
    buAcceptor.addStep("*((S", ParserState.z, "))", gREpsilon);
    buAcceptor.addStep("*((S)", ParserState.z, ")", null);
    buAcceptor.addStep("*(S", ParserState.z, ")", gRBrackets);
    buAcceptor.addStep("*(S)", ParserState.z, "", null);
    buAcceptor.addStep("*S", ParserState.z, "", gRBrackets);
    buAcceptor.addStep("*S", ParserState.zf, "", null);

    Gson gson = new Gson();
    String bUAcceptorJson = gson.toJson(buAcceptor);
    System.out.print(bUAcceptorJson);

  }

}
