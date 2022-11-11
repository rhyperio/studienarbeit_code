import de.dhbw.karlsruhe.derivation.tree.validation.CollectGrammarRules
import de.dhbw.karlsruhe.derivation.tree.validation.DerivationTreeValidation
import de.dhbw.karlsruhe.derivation.tree.validation.SetupValidationTree

fun main(args: Array<String>) {

    val collectGrammarRules: CollectGrammarRules = CollectGrammarRules()
    val setupValidationTree: SetupValidationTree = SetupValidationTree();
    val derivationTreeValidation: DerivationTreeValidation = DerivationTreeValidation(collectGrammarRules)

    println(derivationTreeValidation.checkTree(setupValidationTree.root()))

}
