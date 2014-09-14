drop table ef_user;
drop table ef_match;
drop table ef_roles;
drop table ef_prediction;
drop table ef_team;

create table ef_user(
id bigint primary key,
name varchar(255),
user_id varchar(10),
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
global_team_points int,
global_goal_points int
);

create table ef_roles(
user_id varchar(10),
role varchar(20)
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


create table ef_match(
id bigint  primary key ,
team_a bigint,
team_a_score int,
team_b bigint,
team_b_score int,
match_date timestamp,
match_type varchar(50)
);



