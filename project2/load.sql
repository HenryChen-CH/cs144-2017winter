load data local infile "Sellers.csv" into table Sellers
fields terminated by '|*|';

load data local infile "Bidders.csv" into table Bidders
fields terminated by '|*|';

load data local infile "Items.csv" into table Items
fields terminated by '|*|';

load data local infile "Bids.csv" into table Bids
fields terminated by '|*|';

load data local infile "Categories.csv" into table Categories
fields terminated by '|*|';
