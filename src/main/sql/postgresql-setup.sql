drop sequence hibernate_sequence;
create sequence hibernate_sequence increment by 1 minvalue 1;

drop table ef_user;
drop table ef_match;
drop table ef_roles;
drop table ef_league;
drop table ef_tournament;
drop table ef_user_league;
drop table ef_prediction;
drop table ef_team;

create table ef_user(
    id bigint primary key,
    name varchar(255),
    user_id varchar(10) UNIQUE Not NULL,
    password varchar(255),
    email varchar(255),
    points bigint,
    status varchar(20),
    joining_date timestamp,
    last_updated timestamp,
    team_winner bigint,
    team_runner bigint,
    goals int,
    subscription varchar(20),
    global_team_points bigint,
    global_goal_points bigint
);

insert into ef_user(id,name,user_id,password,email,joining_date,points,global_team_points,global_goal_points) values (nextval('hibernate_sequence'),'Varesh Tapadia','vtapa', '4da02ae075d61ec42a523160856da41029a3d7990305e43196f29f8cedfbb41038302a86a3aeef6a', 'vtapa@nets.eu',current_timestamp,0,0,0);

--update ef_user set password='4da02ae075d61ec42a523160856da41029a3d7990305e43196f29f8cedfbb41038302a86a3aeef6a';

create table ef_roles(
    user_id varchar(10),
    role varchar(20)
);

insert into ef_roles(user_id,role) values ('vtapa','USER');
insert into ef_roles(user_id,role) values ('vtapa','ADMIN');

create table ef_user_league(
    user_id bigint,
    league_id bigint
);

create table ef_league(
    id bigint  primary key ,
    name varchar(255),
    league_owner bigint,
    base_amount int
);

create table ef_prediction(
    id bigint primary key,
    user_id bigint,
    match_id bigint,
    team_a_score int,
    team_b_score int,
    created timestamp,
    last_updated timestamp,
    points int
);

create table ef_team (
    id bigint primary key,
    rank bigint,
    points int,
    name varchar(245) not null,
    image varchar(50),
    status varchar(20),
    team_group varchar(50),
    goals_for int,
    goals_against int
);

create table ef_tournament(
    id bigint  primary key ,
    name varchar(255)
);

create table ef_match(
    id bigint  primary key,
    tournament bigint,
    team_winner bigint,
    team_a bigint,
    team_a_score int,
    team_a_penalty int,
    team_b bigint,
    team_b_score int,
    team_b_penalty int,
    match_date timestamp,
    match_type varchar(50)
);

commit;
