set timezone = 'Asia/Kolkata';
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
update ef_user set subscription ='FULL';
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
    tournament bigint,
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

create table ef_tournament (
    id bigint  primary key ,
    name varchar(255),
    status varchar(20),
    start_date timestamp
);

insert into ef_tournament values (1,'Hero ISL','ACTIVE', '12-oct-2014 13:30');
update ef_tournament set status ='ACTIVE';

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

insert into ef_team (id,points,name,image,status,team_group,goals_for,goals_against) values (1,0,'Atletico De Kolkata','kol','ACTIVE','League',0,0);
insert into ef_team (id,points,name,image,status,team_group,goals_for,goals_against) values (2,0,'Club Chennai','che','ACTIVE','League',0,0);
insert into ef_team (id,points,name,image,status,team_group,goals_for,goals_against) values (3,0,'Delhi Dynamos FC','del','ACTIVE','League',0,0);
insert into ef_team (id,points,name,image,status,team_group,goals_for,goals_against) values (4,0,'FC Goa','goa','ACTIVE','League',0,0);
insert into ef_team (id,points,name,image,status,team_group,goals_for,goals_against) values (5,0,'FC Pune City','pun','ACTIVE','League',0,0);
insert into ef_team (id,points,name,image,status,team_group,goals_for,goals_against) values (6,0,'Kerala Blasters FC','ker','ACTIVE','League',0,0);
insert into ef_team (id,points,name,image,status,team_group,goals_for,goals_against) values (7,0,'Mumbai City FC','mum','ACTIVE','League',0,0);
insert into ef_team (id,points,name,image,status,team_group,goals_for,goals_against) values (8,0,'NorthEast United FC','neu','ACTIVE','League',0,0);

update ef_team set rank=id;
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
    match_date timestamptz,
    match_type varchar(50)
);

insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (2,1,1,7,'12-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (3,1,3,5,'13-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (4,1,8,6,'15-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (5,1,4,2,'16-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (6,1,7,5,'17-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (7,1,1,3,'18-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (8,1,2,6,'19-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (9,1,8,4,'20-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (10,1,5,6,'22-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (11,1,4,1,'23-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (12,1,7,8,'24-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (13,1,3,2,'25-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (14,1,5,4,'26-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (15,1,6,1,'27-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (16,1,3,8,'28-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (17,1,2,7,'29-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (18,1,4,3,'31-oct-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (19,1,7,6,'01-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (20,1,2,1,'02-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (21,1,5,8,'03-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (22,1,6,4,'04-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (23,1,7,3,'05-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (24,1,8,2,'06-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (25,1,1,5,'07-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (26,1,6,3,'08-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (27,1,4,7,'09-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (28,1,8,1,'10-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (29,1,5,2,'11-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (30,1,3,4,'12-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (31,1,6,7,'13-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (32,1,1,2,'14-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (33,1,8,5,'15-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (34,1,3,6,'16-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (35,1,7,4,'17-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (36,1,1,8,'18-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (37,1,2,5,'19-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (38,1,8,7,'21-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (39,1,1,4,'22-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (40,1,2,3,'23-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (41,1,6,5,'24-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (42,1,2,8,'26-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (43,1,4,6,'27-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (44,1,3,7,'28-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (45,1,5,1,'29-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (46,1,6,2,'30-nov-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (47,1,4,8,'01-dec-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (48,1,3,1,'02-dec-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (49,1,5,7,'03-dec-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (50,1,6,8,'04-dec-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (51,1,2,4,'05-dec-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (52,1,5,3,'06-dec-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (53,1,7,1,'07-dec-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (54,1,8,3,'09-dec-2014 16:30','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (55,1,4,5,'09-dec-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (56,1,1,6,'10-dec-2014 16:30','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (57,1,7,2,'10-dec-2014 19:00','League');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (58,1,null,null,'13-dec-2014 19:00','SemiFinal');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (59,1,null,null,'14-dec-2014 19:00','SemiFinal');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (60,1,null,null,'16-dec-2014 19:00','SemiFinal');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (61,1,null,null,'17-dec-2014 19:00','SemiFinal');
insert into ef_match (id,tournament,team_a,team_b,match_date,match_type) values (62,1,null,null,'20-dec-2014 19:00','Final');

commit;