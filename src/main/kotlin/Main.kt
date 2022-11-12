import de.dhbw.karlsruhe.derivation.tree.validation.CollectGrammarRules
import de.dhbw.karlsruhe.derivation.tree.validation.DerivationTreeValidation
import de.dhbw.karlsruhe.derivation.tree.validation.SetupValidationTree
import java.io.File
import java.util.*

fun main(args: Array<String>) {

    val grammarAsJson = Scanner(
        File("src/main/resources/test_examples/exampleGrammar.json")
    ).useDelimiter("\\Z")
        .next()
    val treeAsJson = Scanner(
        File(
            "src/main/resources/test_examples/exampleDerivationTree.json"
        )
    ).useDelimiter(
        "\\Z"
    )
        .next()

    val collectGrammarRules: CollectGrammarRules = CollectGrammarRules(grammarAsJson)
    val setupValidationTree: SetupValidationTree = SetupValidationTree(treeAsJson);
    val derivationTreeValidation: DerivationTreeValidation =
        DerivationTreeValidation(collectGrammarRules)

    println(derivationTreeValidation.checkTree(setupValidationTree.root()))

}
