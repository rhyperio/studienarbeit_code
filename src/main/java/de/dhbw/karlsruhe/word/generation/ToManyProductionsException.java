package de.dhbw.karlsruhe.word.generation;

public class ToManyProductionsException extends Exception{

    ToManyProductionsException(int productionLimit){
        super("Genutzte Produktionen überschreiten das Limit von "+ productionLimit+" Produktionen!");
    }
}
