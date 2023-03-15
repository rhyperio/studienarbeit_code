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
  }

}
