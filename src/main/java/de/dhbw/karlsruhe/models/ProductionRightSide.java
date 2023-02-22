package de.dhbw.karlsruhe.models;

import java.util.List;
import java.util.Random;

public enum ProductionRightSide {
    p1("t"),
    p2("N"),
    p3("t N"),
    p4("t t N"),
    p5("epsilon"),
    p6("N t"),
    p7("N t t"),
    p8("t N t"),
    p9("t t N t"),
    p10("t N t t"),
    p11("t t N t t"),
    p12("t t"),
    p13("t t t");

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
