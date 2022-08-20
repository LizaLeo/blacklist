package ee.lhv.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PersonMatchingTest {

    private static final String BLACKLIST_NAME = "Osama Beans Laden";
    private InputStream blackListInputStream;
    private InputStream noiseListInputStream;

    @BeforeEach
    void setUp() {
        blackListInputStream = new ByteArrayInputStream(BLACKLIST_NAME.getBytes());
        noiseListInputStream = new ByteArrayInputStream(",".getBytes());
    }

    @Test
    void testDeNoiseName() {
        Person person = new Person("SOme , name is,it");
        person.deNoiseName(noiseListInputStream);
        assertEquals("SOme name is it", person.denoisedName);
    }

    private static Stream<Arguments> testFindMatchesArgumentsProvider() {
        return Stream.of(
                Arguments.of("Osama Laden", 67),
                Arguments.of("Osama Beans Laden", 100),
                Arguments.of("Beans Laden, Osama", 100),
                Arguments.of("Laden Osama Beans", 100),
                Arguments.of("to the osama beans laden", 100),
                Arguments.of("osama and beaNs laden", 100),
                Arguments.of("OsamaLaden", 67),
                Arguments.of("OsamaBeans Laden", 100),
                Arguments.of("BeansLaden, Osama", 100),
                Arguments.of("Laden OsamaBeans", 100),
                Arguments.of("to the osama beansladen", 100),
                Arguments.of("osama and beaNsladen", 100),
                Arguments.of("OsamaTEST123Laden test", 45),
                Arguments.of("Osama B Laden", 83),
                Arguments.of("BeansLaden, O", 83),
                Arguments.of("L OsamaBeans", 83),
                Arguments.of("Osama B L", 66),
                Arguments.of("Osama B Lux", 50)
        );
    }

    @ParameterizedTest
    @MethodSource("testFindMatchesArgumentsProvider")
    void testFindMatches(String name, int matchesPercentage) {
        Person person = new Person(name);
        person.deNoiseName(noiseListInputStream);
        List<MatchResult> results = person.findNameMatchesInBlackList(blackListInputStream, 45);
        assertEquals(BLACKLIST_NAME, results.get(0).getBlackListName());
        assertEquals(matchesPercentage, results.get(0).getMatchPercent());
    }

    private static Stream<Arguments> testMatchWholeWordArgumentsProvider() {
        return Stream.of(
                Arguments.of("Osama Laden", 67),
                Arguments.of("Osama Beans Laden", 100),
                Arguments.of("Beans Laden, Osama", 100),
                Arguments.of("Laden Osama Beans", 100),
                Arguments.of("to the osama beans laden", 100),
                Arguments.of("osama and beaNs laden", 100),
                Arguments.of("ladenbeanosa ma", 0)
        );
    }

    @ParameterizedTest
    @MethodSource("testMatchWholeWordArgumentsProvider")
    void testMatchWholeWord(String name, double percentage) {
        Person person = new Person(name);
        person.deNoiseName(noiseListInputStream);
        String[] blackListParts = BLACKLIST_NAME.split(" ");

        assertEquals(percentage, person.patternMatchWholeWord(blackListParts));
    }

    private static Stream<Arguments> testMatchWordsWithOmittedSpaceArgumentsProvider() {
        return Stream.of(
                Arguments.of("OsamaLaden", 67),
                Arguments.of("OsamaBeans Laden", 67),
                Arguments.of("BeansLaden, Osama", 67),
                Arguments.of("Laden OsamaBeans", 67),
                Arguments.of("to the osama beansladen", 67),
                Arguments.of("osama and beaNsladen", 67),
                Arguments.of("OsamaTEST123Laden test", 45),
                Arguments.of("ladenbeanosa ma", 0)
        );
    }

    @ParameterizedTest
    @MethodSource("testMatchWordsWithOmittedSpaceArgumentsProvider")
    void testWordsWithOmittedSpace(String name, int percentage) {
        Person person = new Person(name);
        person.deNoiseName(noiseListInputStream);
        String[] blackListParts = BLACKLIST_NAME.split(" ");

        assertEquals(percentage, person.patternMatchWordsWithOmittedSpace(blackListParts));
    }

    private static Stream<Arguments> testMatchInitialsArgumentsProvider() {
        return Stream.of(
                Arguments.of("OsamaLaden", 0),
                Arguments.of("Osama B Laden", 16),
                Arguments.of("BeansLaden, O", 16),
                Arguments.of("L OsamaBeans", 16)
        );
    }

    @ParameterizedTest
    @MethodSource("testMatchInitialsArgumentsProvider")
    void testMatchInitials(String name, int percentage) {
        Person person = new Person(name);
        person.deNoiseName(noiseListInputStream);
        String[] blackListParts = BLACKLIST_NAME.split(" ");

        assertEquals(percentage, person.patternMatchInitials(blackListParts));
    }


}