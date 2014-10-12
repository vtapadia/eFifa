package com.vtapadia.fifa.resource;

import com.vtapadia.fifa.domain.Tournament;

public class LeagueResource extends BasicResponse {
    public long id;
    public String name;
    public String tournamentName;
    public UserResource leagueOwner;
    public int baseAmount;
}
