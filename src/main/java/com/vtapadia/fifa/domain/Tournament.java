package com.vtapadia.fifa.domain;

import javax.persistence.*;

@Entity
@Table(name = "ef_tournament")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

}
