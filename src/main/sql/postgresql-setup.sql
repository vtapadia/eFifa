create table ef_user(
id int primary key,
name varchar(255),
user_id varchar(10),
password varchar(255),
email varchar(255),
points int,
status varchar(20),
joining_date timestamp,
last_updated timestamp,
team_winner int,
team_runner int,
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
id int primary key,
user_id int,
match_id int,
team_a_score int,
team_b_score int,
created timestamp,
last_updated timestamp,
points int
);

create table ef_team (
id int primary key,
rank int,
points int,
name varchar(245) not null,
image varchar(50),
status varchar(20),
team_group varchar(50),
goals_for int,
goals_against int
);


create table ef_match(
id int  primary key ,
team_a int,
team_a_score int,
team_b int,
team_b_score int,
match_date timestamp,
match_type varchar(50)
);



