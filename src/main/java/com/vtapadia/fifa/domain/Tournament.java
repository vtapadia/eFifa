package com.vtapadia.fifa.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ef_tournament")
public class Tournament {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    public String name;

    @Enumerated(EnumType.STRING)
    public Status status;

    @Column(name = "start_date")
    public Date startDate;
}
