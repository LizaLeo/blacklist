package ee.lhv.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MatchUtilsTest {

    @Test
    void arrayWeight() {
        String[] arr = new String[]{"Osama", "Bin's", "Laden"};
        assertEquals(15, MatchUtils.arrayWeight(arr));
    }

    @Test
    void wordWeightInArray() {
        int nameLength = 15;
        String word = "Osama";
        assertEquals(33.33, MatchUtils.wordWeightInArray(nameLength, word));
    }
}