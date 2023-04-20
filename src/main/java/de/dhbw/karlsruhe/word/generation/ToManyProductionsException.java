package de.dhbw.karlsruhe.word.generation;

public class ToManyProductionsException extends Exception{

    ToManyProductionsException(int productionLimit){
        super("Genutzte Produktionen überschreiten das Limit von "+ productionLimit+" Produktionen!");
    }

    ToManyProductionsException(int readLimit, int actionLimit){
        super("Genutzte Produktionen überschreiten das Limit der Leseproduktionen von "+ readLimit+" oder der Aktionsproduktionen von "+actionLimit+"!");
    }
}
