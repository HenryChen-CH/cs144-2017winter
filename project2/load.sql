load data local infile "Sellers.dat" into table Sellers
fields terminated by '|*|';

load data local infile "Bidders.dat" into table Bidders
fields terminated by '|*|';

load data local infile "Items.dat" into table Items
fields terminated by '|*|';

load data local infile "Bids.dat" into table Bids
fields terminated by '|*|';

load data local infile "Categories.dat" into table Categories
fields terminated by '|*|';
