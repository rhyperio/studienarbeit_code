package de.dhbw.karlsruhe.word.generation;

public class WordLimitationsNotFulfillableException extends Exception{

    WordLimitationsNotFulfillableException(){
        super("Es konnte mit der gegebenen Grammatik kein Wort in den angegebenen Limitationen erzeugt werden!");
    }
}
