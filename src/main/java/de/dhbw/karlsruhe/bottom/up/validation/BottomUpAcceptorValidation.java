package de.dhbw.karlsruhe.bottom.up.validation;

import de.dhbw.karlsruhe.bottom.up.models.BottomUpAcceptor;
import de.dhbw.karlsruhe.services.GrammarService;

public class BottomUpAcceptorValidation {

    private GrammarService grammerService;

    public BottomUpAcceptorValidation(String grammarAsJson) {
        this.grammerService = new GrammarService(grammarAsJson);
    }

    public boolean checkAcceptor(BottomUpAcceptor bUAcceptor, String word) {
        return word != null;
    }
}
