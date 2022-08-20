package ee.lhv.test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

public class MatchUtils {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    private MatchUtils() {
    }

    public static int arrayWeight(String[] arr) {
        return Arrays.stream(arr).map(String::length).reduce(0, Integer::sum);
    }

    public static double wordWeightInArray(int nameWeight, String word) {
        BigDecimal bigDecimal = BigDecimal.valueOf(((double) word.length() / nameWeight)).multiply(ONE_HUNDRED);
        return bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
