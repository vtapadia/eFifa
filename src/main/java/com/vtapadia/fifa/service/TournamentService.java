package com.vtapadia.fifa.service;

import com.vtapadia.fifa.dao.TournamentDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TournamentService {

    @Autowired
    TournamentDAO tournamentDAO;

    public String getCurrentTournament() {
        return tournamentDAO.getCurrentTournament().name;
    }
}
