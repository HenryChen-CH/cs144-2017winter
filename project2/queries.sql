select count(*) from (
    select UserID from Sellers
    union
    select UserID from Bidders
) as tmp;

select count(*) from Items where binary Location='New York';

select count(*) from (
    select ItemID from Categories group by ItemID
    having count(Category)=4
) as tmp;

select ItemID from (
    select ItemID, max(Currently)
    from Items
    where Ends > '2001-12-20 12:00:01'
) as tmp;

select count(*) from Sellers where Rating > 1000;

select count(*) from Sellers inner join Bidders
on Sellers.UserID=Bidders.UserID;

select count(*) from (
    select Category from
    Categories inner join Bids
    on Categories.ItemID=Bids.ItemID
    group by Category
    having count(Amount) > 0 and max(Amount)> 100
) as tmp;
