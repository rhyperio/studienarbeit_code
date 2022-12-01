package de.dhbw.karlsruhe.models;

import java.util.List;
import java.util.Random;

public enum ProductionRightSide {
    p1("t", true),
    p2("N", false),
    p3("t N", false),
    p4("t t N", false),
    p5("epsilon", true);

    private static final List<ProductionRightSide> VALUES =
            List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    public final String rightSide;
    public final boolean isEndProduction;


    ProductionRightSide(String rightSide, boolean isEndProduction) {
        this.rightSide = rightSide;
        this.isEndProduction = isEndProduction;
    }
    public static ProductionRightSide randomProduction(){
        return VALUES.get(RANDOM.nextInt(SIZE));
    }
}
