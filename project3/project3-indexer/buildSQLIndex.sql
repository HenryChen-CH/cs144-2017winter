create table Locations (
    ItemID integer,
    location Point not null,
    spatial index(Location),
    foreign key (ItemID) references Items(ItemID)
) ENGINE = MyISAM;

Insert into Locations(ItemID, Location)
select ItemID, Point(Latitude, Longitude) from Items
where Latitude is not null and Longitude is not null;
