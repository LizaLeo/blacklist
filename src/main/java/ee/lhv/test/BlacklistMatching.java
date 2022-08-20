package ee.lhv.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BlacklistMatching {

    private static final Logger logger = LogManager.getLogger(BlacklistMatching.class);

    public static void main(String[] args) {
        String nameToCheck;
        String blackListFileName = args.length > 1 ? args[1] : "blacklist.txt";
        String noiseListFileName = args.length > 2 ? args[2] : "noiselist.txt";
        int matchPercent = args.length >= 3 ? Integer.parseInt(args[3]) : 50;

        if (args.length == 0) {
            throw new IllegalArgumentException("Name to check required!");
        }
        nameToCheck = args[0];

        logger.info("NAME TO CONTROL: {}", nameToCheck);
        logger.info("FILE WITH BLACKLIST: {}", blackListFileName);
        logger.info("FILE WITH NOISE WORDS: {}", noiseListFileName);
        logger.info("PERCENT TO ALARM MATCH: {}", matchPercent);

        Person person = new Person(nameToCheck);
        person.deNoiseName(noiseListFileName);
        List<MatchResult> blackListMatches = person.findNameMatchesInBlackList(blackListFileName, matchPercent);

        logger.info("MATCHED RESULTS:");
        if (blackListMatches.isEmpty()) {
            logger.info("No matches found");
        } else {
            blackListMatches.forEach(logger::info);
        }
    }

}
