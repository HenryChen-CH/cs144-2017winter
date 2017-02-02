create table Actors (Name VARCHAR(40), Movie VARCHAR(80), Year INTEGER, Role VARCHAR(40));
load data local infile '~/data/actors.csv' into table Actors FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"';
select Name from Actors where Movie='Die Another Day';
drop table Actors;
