package ee.lhv.test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Person {

    private static final int FULL_MATCH = 100;

    // word A, word B, word X - between A and B
    // AB and AXB Word Distance is 1. (3 - 1)/3 = 0.66
    private static final double WORD_BETWEEN_MATCH = 0.66;

    // A is initial, AX is whole name
    // we can take like distance between them is 1
    // (2 - 1)/2 = 0.5
    private static final double INITIAL_MATCH = 0.5;

    String name;
    String denoisedName;

    public Person(String name) {
        this.name = name;
    }

    void deNoiseName(String noiseListFileName) {
        deNoiseName(FileUtils.getFile(noiseListFileName));
    }

    void deNoiseName(InputStream noiseListInputStream) {
        String denoisedString = this.name;
        try (InputStreamReader streamReader = new InputStreamReader(noiseListInputStream, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                denoisedString = denoisedString.replaceAll(line.strip(), " ");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.denoisedName = denoisedString.replaceAll("\\s+", " ");
    }

    List<MatchResult> findNameMatchesInBlackList(String blackListFileName, int alarmPercent) {
        InputStream is = FileUtils.getFile(blackListFileName);
        return findNameMatchesInBlackList(is, alarmPercent);
    }

    List<MatchResult> findNameMatchesInBlackList(InputStream is, int alarmPercent) {
        List<MatchResult> matches = new ArrayList<>();
        try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(streamReader)) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] blackListParts = line.split(" ");

                int matchPercentage = patternMatchWholeWord(blackListParts);

                if (matchPercentage < FULL_MATCH) {
                    matchPercentage += patternMatchWordsWithOmittedSpace(blackListParts);
                }
                if (matchPercentage < FULL_MATCH) {
                    matchPercentage += patternMatchInitials(blackListParts);
                }

                if (matchPercentage >= alarmPercent) {
                    matches.add(new MatchResult(line, Math.min(matchPercentage, FULL_MATCH)));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return matches;
    }

    int patternMatchWholeWord(String[] blackListParts) {
        int nameWeight = MatchUtils.arrayWeight(blackListParts);
        double matchPersentage = 0;

        for (String word : blackListParts) {
            String patternString = "\\b(" + word + ")\\b";
            Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            matchPersentage += pattern.matcher(this.denoisedName).find() ? MatchUtils.wordWeightInArray(nameWeight, word) : 0;
        }
        return (int) Math.min(Math.ceil(matchPersentage), FULL_MATCH);
    }

    int patternMatchWordsWithOmittedSpace(String[] blackListParts) {
        double matchPersentage = 0;
        int nameWeight = MatchUtils.arrayWeight(blackListParts);

        for (int i = 0; i < blackListParts.length; i++) {
            for (int j = 0; j < blackListParts.length; j++) {
                if (j == i) continue;
                String multiPartWord = blackListParts[i] + blackListParts[j];
                String omittedSpaceAndSymbolsBetween = "\\b(" + blackListParts[i] + "([\\w\\d]*)" + blackListParts[j] + ")\\b";
                Pattern pattern = Pattern.compile(omittedSpaceAndSymbolsBetween, Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(this.denoisedName);

                double wordsWeightInArray = MatchUtils.wordWeightInArray(nameWeight, multiPartWord);
                if (matcher.find()) {
                    String betweenWord = matcher.group(2);
                    matchPersentage += betweenWord.isBlank() ? wordsWeightInArray : wordsWeightInArray * WORD_BETWEEN_MATCH;
                }
            }
        }
        return (int) Math.min(Math.ceil(matchPersentage), FULL_MATCH);
    }

    int patternMatchInitials(String[] blackListParts) {
        int matchCounter = 0;
        int nameWeight = MatchUtils.arrayWeight(blackListParts);

        for (String blackListPart : blackListParts) {
            char ch = blackListPart.charAt(0);
            String patternString = "\\b(" + ch + ")\\b";
            Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
            matchCounter += pattern.matcher(this.denoisedName).find() ?
                    MatchUtils.wordWeightInArray(nameWeight, blackListPart) * INITIAL_MATCH : 0;

        }
        return matchCounter;
    }


}
