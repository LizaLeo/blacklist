package ee.lhv.test;

public class MatchResult {

    private final String blackListName;
    private final int matchPercent;

    public MatchResult(String blackListName, int matchPercent) {
        this.blackListName = blackListName;
        this.matchPercent = matchPercent;
    }

    public String getBlackListName() {
        return blackListName;
    }

    public int getMatchPercent() {
        return matchPercent;
    }

    @Override
    public String toString() {
        return "MatchResults{" +
                "blackListName='" + blackListName + '\'' +
                ", matchPercent=" + matchPercent + "%" +
                '}';
    }
}
