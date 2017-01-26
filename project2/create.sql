create table Items(
    ItemID integer NOT NULL,
    Name varchar(50) NOT NULL,
    Currently decimal(8,2) NOT NULL,
    Buy_Price decimal(8,2),
    First_Bid decimal(8,2) NOT NULL,
    Location varchar(100) NOT NULL,
    Latitude decimal(9,6),
    Longitude decimal(9,6),
    Country varchar(100) NOT NULL,
    Started timestamp NOT NULL,
    Ends timestamp NOT NULL,
    SellerID varchar(50) NOT NULL,
    Description varchar(4000) not null,
    primary key(ItemID)
)ENGINE = INNODB;

create table Bids(
    ItemID integer not null,
    UserID varchar(50) not null,
    BidTime timestamp not null,
    Amount decimal(8,2) not null,
    primary key(ItemID, UserID, BidTime)
)ENGINE = INNODB;

create table Categories(
    ItemID integer not null,
    Category varchar(100) not null,
    primary key(ItemID, Category)
)ENGINE = INNODB;

create table Sellers(
    UserID varchar(50) not null,
    Rating integer not null,
    primary key(UserID)
)ENGINE = INNODB;

create table Bidders(
    UserID varchar(50) not null,
    Rating integer not null,
    Address varchar(100) not null,
    Country varchar(50) not null,
    primary key(UserID)
)ENGINE = INNODB;
