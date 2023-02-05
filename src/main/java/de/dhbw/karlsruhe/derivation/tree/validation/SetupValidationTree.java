package de.dhbw.karlsruhe.derivation.tree.validation;

import com.google.gson.Gson;
import de.dhbw.karlsruhe.derivation.tree.models.DerivationTree;

public class SetupValidationTree {

  private final String json;

  public SetupValidationTree(String json) {
    this.json = json;
  }

  public DerivationTree root() {
    return getTreeFromJson();
  }

  private DerivationTree getTreeFromJson() {
    Gson gson = new Gson();
    return gson.fromJson(json, DerivationTree.class);
  }

}
