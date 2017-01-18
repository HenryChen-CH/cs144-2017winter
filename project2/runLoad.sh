#!/bin/bash

# Run the drop.sql batch file to drop existing tables
# Inside the drop.sql, you sould check whether the table exists. Drop them ONLY if they exists.
mysql CS144 < drop.sql

# Run the create.sql batch file to create the database and tables
mysql CS144 < create.sql

# Compile and run the parser to generate the appropriate load files
ant run-all

# If the Java code does not handle duplicate removal, do this now
sort Items.csv -u -o Items.csv
sort Bids.csv -u -o Bids.csv
sort Categories.csv -u -o Categories.csv
sort Sellers.csv -u -o Sellers.csv
sort Bidders.csv -u -o Bidders.csv

# Run the load.sql batch file to load the data
mysql CS144 < load.sql

# Remove all temporary files
rm *.csv
