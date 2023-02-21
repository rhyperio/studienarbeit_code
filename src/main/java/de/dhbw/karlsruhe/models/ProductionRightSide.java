package de.dhbw.karlsruhe.models;

import java.util.List;
import java.util.Random;

public enum ProductionRightSide {
    p1("t"),
    p2("N"),
    p3("t N"),
    p4("t t N"),
    p5("epsilon");

    private static final List<ProductionRightSide> VALUES =
            List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final String rightSide;


    ProductionRightSide(String rightSide) {
        this.rightSide = rightSide;
    }
    public static ProductionRightSide randomProduction(){
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
