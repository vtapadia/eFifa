package com.vtapadia.fifa.domain;

import javax.persistence.*;

@Entity
@Table(name = "ef_league")
public class League {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "league_owner", nullable = false)
    private User leagueOwner;

    @Column(name = "base_amount")
    private int baseAmount;

}
