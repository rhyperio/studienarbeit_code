package de.dhbw.karlsruhe.derivation.tree.validation;

import com.google.gson.Gson;
import de.dhbw.karlsruhe.derivation.tree.models.DerivationTree;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class SetupValidationTree {

  public DerivationTree root() {
    try {
      return getTreeFromJson();
    } catch (FileNotFoundException e) {
      System.out.println(e.getMessage());
    }
    return null;
  }

  private DerivationTree getTreeFromJson() throws FileNotFoundException {
    Gson gson = new Gson();
    Reader reader = new FileReader("src/main/resources/test_examples/exampleDerivationTree.json");
    return gson.fromJson(reader, DerivationTree.class);
  }

}
