package de.dhbw.karlsruhe.models;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum ProductionRightSide {
    p1("t", true),
    p2("N", false),
    p3("t N", false),
    p4("t t N", false),
    p5("epsilon", true),
    p6("N t", false),
    p7("N t t", false),
    p8("t N t", false),
    p9("t t N t", false),
    p10("t N t t", false),
    p11("t t N t t", false),
    p12("t t", true),
    p13("t t t", true);

    private static final List<ProductionRightSide> VALUES =
            List.of(values());
    private static final List<ProductionRightSide> ENDPRODUCTIONS =
            Arrays.stream(values()).filter(v -> v.isEndProduction).toList();
    private static final int SIZE = VALUES.size();
    private static final int SIZEENDPRODUCTIONS = ENDPRODUCTIONS.size();
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

    public static ProductionRightSide randomEndProduction(){
        return ENDPRODUCTIONS.get(RANDOM.nextInt(SIZEENDPRODUCTIONS));
    }
}
