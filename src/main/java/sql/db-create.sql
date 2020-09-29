CREATE DATABASE practice8;

USE practice8;

CREATE TABLE users (

 id INT PRIMARY KEY AUTO_INCREMENT,

 login VARCHAR(10) UNIQUE

);

CREATE TABLE teams (

 id INT PRIMARY KEY AUTO_INCREMENT,

 name VARCHAR(10)

);

CREATE TABLE users_teams (

 user_id INT REFERENCES users(id) ON DELETE CASCADE,

 team_id INT REFERENCES teams(id) ON DELETE CASCADE,

 UNIQUE (user_id, team_id)

);

insert into users(login) values("asasa");
insert into users(login) values("vvv");
insert into users(login) values("bbbb");

insert into teams(name) values("first");
insert into teams(name) values("second");

insert into users_teams value(1,1);
insert into users_teams value(2,2);