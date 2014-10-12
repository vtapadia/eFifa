package com.vtapadia.fifa.domain;


public enum MatchType {
    League,
    Group,
    RoundOf16(2),
    QuarterFinal(3),
    SemiFinal(4),
    Final(4);

    int multiplier = 1;

    private MatchType() {}

    private MatchType(int multiplier) {
        this.multiplier = multiplier;
    }

    public int getMultiplier() {
        return multiplier;
    }
}
