package de.dhbw.karlsruhe.word.generation;

public class ToManyProductionsException extends Exception{

    ToManyProductionsException(int productionLimit){
        super("Used productions exceeded the limit of "+ productionLimit+"!");
    }
}
