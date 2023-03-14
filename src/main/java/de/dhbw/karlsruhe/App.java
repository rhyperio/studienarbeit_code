package de.dhbw.karlsruhe;

import com.google.gson.Gson;
import de.dhbw.karlsruhe.bottom.up.models.BottomUpAcceptor;
import de.dhbw.karlsruhe.derivation.tree.validation.DerivationTreeValidation;
import de.dhbw.karlsruhe.derivation.tree.validation.SetupValidationTree;
import de.dhbw.karlsruhe.models.GrammarProduction;
import de.dhbw.karlsruhe.models.ParserState;
import de.dhbw.karlsruhe.util.Resource;

import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);

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
      System.out.println(derivationTreeValidation.checkTree(setupValidationTree.root(), "(())"));
    } else {
      System.out.println("Json could not be read.");
    }


    // Test for BUAcceptor Creation

    GrammarProduction gREpsilon = new GrammarProduction("S", "epsilon");
    GrammarProduction gRBrackets = new GrammarProduction("S", "( S )");
    GrammarProduction gRDouble = new GrammarProduction("S", "S S");

    BottomUpAcceptor buAcceptor = new BottomUpAcceptor();
    buAcceptor.addStep("*", ParserState.Z0, "(())", null);
    buAcceptor.addStep("*(", ParserState.Z, "())", null);
    buAcceptor.addStep("*((", ParserState.Z, "))", null);
    buAcceptor.addStep("*((S", ParserState.Z, "))", gREpsilon);
    buAcceptor.addStep("*((S)", ParserState.Z, ")", null);
    buAcceptor.addStep("*(S", ParserState.Z, ")", gRBrackets);
    buAcceptor.addStep("*(S)", ParserState.Z, "", null);
    buAcceptor.addStep("*S", ParserState.Z, "", gRBrackets);
    buAcceptor.addStep("*S", ParserState.ZF, "", null);

    Gson gson = new Gson();
    String bUAcceptorJson = gson.toJson(buAcceptor);
    System.out.print(bUAcceptorJson);

  }

}
