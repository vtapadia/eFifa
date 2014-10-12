package com.vtapadia.fifa.domain;

import javax.persistence.*;

@Entity
@Table(name = "ef_league")
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String name;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "tournament", nullable = false)
    public Tournament tournament;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "league_owner", nullable = false)
    public User leagueOwner;

    @Column(name = "base_amount")
    public int baseAmount;
}
