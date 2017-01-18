The design of table is as following:

	1) Items(ItemID, Name, Currently, Buy_Price, First_Bid, Location, Latitude, Longitude, Country, Started, Ends, SellerID, Description), primary key(ItemID), foreign key: SellerID->Sellers.UserID

	2) Bids(ItemId, UserID, BidTime, Amount), primary key(ItemId, UserID, BidTime), foreign key: ItemID->Items.ItemID, UserID->Bidders.UserID

	3) Categories(ItemID, Category), primary key(ItemID, Category), foreign key: ItemID->Items.ItemID

	4) Sellers(UserID, Rating), primary key(UserID)

	5) Bidders(UserID, Rating, Address, Country), primary key(UserID)

The functional dependences:

Items.ItemID -> Name, Currently, Buy_Price, First_Bid, Location, Latitude, Longitude, Country, Started, Ends, SellerID, Description
(ItemId, UserID, Time)-> Amount
Sellers.UserID -> Rating
Bidders.Bidders -> Rating, Address, Country

BCNF:
	Since the left side of all function dependencies are superkey of corresponding table, it satisfies BCNF constraints.


Satisfy Fourth Normal Form (4NF)