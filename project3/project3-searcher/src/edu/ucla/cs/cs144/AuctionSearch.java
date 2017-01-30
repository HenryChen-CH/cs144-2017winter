package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.File;
import java.io.StringWriter;
import java.sql.*;
import java.util.Date;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;

import java.util.HashSet;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	private IndexSearcher searcher;
	private QueryParser parser;
	private static String indexFolder = "/var/lib/lucene/index1/";
	private static String[] colNames = new String[]
            {"Name", "Category", "Currently", "Buy_Price", "First_Bid", "Number_of_Bids", "Bids", "Location", "Country",
                    "Started", "Ends", "Seller","Description"};
	
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		// TODO: Your code here!
		if (query.length() == 0|| (long)numResultsToReturn+numResultsToSkip <= 0 || numResultsToReturn <= 0 || numResultsToSkip < 0) {
			return new SearchResult[0];
		}
		SearchResult[] sr = new SearchResult[0];
		try {
			parser = getParser();
			searcher = getSearcher();
			Query q =  parser.parse(query);
			TopDocs td = searcher.search(q, numResultsToReturn+numResultsToSkip);
			ScoreDoc[] hits = td.scoreDocs;

			numResultsToReturn = hits.length-numResultsToSkip;
			if (numResultsToReturn == 0) return sr;
			sr = new SearchResult[numResultsToReturn];

			for (int i = numResultsToSkip; i < numResultsToSkip+numResultsToReturn; i++) {
				Document doc = searcher.doc(hits[i].doc);
				sr[i-numResultsToSkip] = new SearchResult(String.valueOf(doc.get("ItemID")), doc.get("Name"));
			}
		} catch (IOException e) {
			System.out.println(e);
			return sr;
		} catch (ParseException e) {
			System.out.println(e);
			return sr;
		}
		return sr;
	}

	public SearchResult[] spatialSearch(String query, SearchRegion region,
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
        if (query.length() == 0|| (long)numResultsToReturn+numResultsToSkip <= 0 || numResultsToReturn <= 0 || numResultsToSkip < 0) {
            return new SearchResult[0];
        }

        SearchResult[] sr = new SearchResult[0];
        Connection conn;
        try {
            conn = DbManager.getConnection(true);
            HashSet<Integer> items = new HashSet<Integer>();
            PreparedStatement stmt  =
                    conn.prepareStatement("select ItemID from Locations where MBRContains(GeomFromText('Polygon(("
                    +region.getLx()+" "+region.getLy()+","
                    +region.getRx()+" "+region.getLy()+","
                    +region.getRx()+" "+region.getRy()+","
                    +region.getLx()+" "+region.getRy()+","
                    +region.getLx()+" "+region.getLy()
                    +"))'), Location)");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(rs.getInt("ItemID"));
            }
            ArrayList<SearchResult> result = new ArrayList<SearchResult>();
            SearchResult[] basicResult = basicSearch(query, 0, Integer.MAX_VALUE);
            for (int i = 0; i < basicResult.length && result.size() < numResultsToReturn+numResultsToSkip; i++) {
                if (items.contains(basicResult[i].getItemId())) {
                    result.add(basicResult[i]);
                }
            }
            return result.subList(numResultsToSkip, result.size()).toArray(new SearchResult[0]);
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Error");
            return sr;
        }
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
        Connection conn;
        int id = Integer.parseInt(itemId);

        try {
            conn = DbManager.getConnection(true);
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = docBuilder.newDocument();

            PreparedStatement item_stmt = conn.prepareStatement("select * from Items where ItemID=?");
            PreparedStatement category_stmt = conn.prepareCall("select Category from Categories where ItemID=?");
            PreparedStatement bids_stmt = conn.prepareStatement("select * from Bids where ItemId=?");
            PreparedStatement bidder_stmt = conn.prepareCall("select * from Bidders where binary UserID=?");
            PreparedStatement seller_stmt = conn.prepareCall("select * from Sellers where binary UserID=?");

            item_stmt.setInt(1, id);
            category_stmt.setInt(1, id);
            bids_stmt.setInt(1, id);

            ResultSet cat_rs = category_stmt.executeQuery();
            ResultSet item_rs = item_stmt.executeQuery();
            ResultSet bids_rs = bids_stmt.executeQuery();

            Element item = doc.createElement("Item");
            item.setAttribute("ItemID", itemId);
            doc.appendChild(item);
            Element num_bids = doc.createElement("Number_of_Bids");
            int num = 0;
            if (item_rs.next()) {
                for (String col: colNames) {
                    if (col.equals("Category")) {
                        while (cat_rs.next()) {
                            Element cat = doc.createElement(col);
                            cat.appendChild(doc.createTextNode(cat_rs.getString(col)));
                            item.appendChild(cat);
                        }
                    } else if (col.equals("Currently") || col.equals("Buy_Price") || col.equals("First_Bid")) {
                        Double price = item_rs.getDouble(col);
                        if (price != null && price != 0) {
                            Element tmp = doc.createElement(col);
                            tmp.appendChild(doc.createTextNode("$" + String.format("%.2f", price)));
                            item.appendChild(tmp);
                        }
                    } else if (col.equals("Number_of_Bids")) {
                        item.appendChild(num_bids);
                        num = 0;
                    } else if (col.equals("Bids")) {
                        Element bids = doc.createElement(col);
                        while (bids_rs.next()) {
                            num++;
                            Element bid = doc.createElement("Bid");
                            Element bidder = doc.createElement("Bidder");
                            String bidderID = bids_rs.getString("UserID");
                            bidder_stmt.setString(1, bidderID);
                            ResultSet bidder_rs = bidder_stmt.executeQuery();
                            if (bidder_rs.next()) {
                                bidder.setAttribute("Rating", String.valueOf(bidder_rs.getInt("Rating")));
                                String location = bidder_rs.getString("Address");
                                String country = bidder_rs.getString("Country");
                                if (location != null && location.length() != 0) {
                                    Element locati = doc.createElement("Location");
                                    locati.appendChild(doc.createTextNode(location));
                                    bidder.appendChild(locati);
                                }
                                if (country != null && country.length() != 0) {
                                    Element cou = doc.createElement("Country");
                                    cou.appendChild(doc.createTextNode(country));
                                    bidder.appendChild(cou);
                                }
                            }
                            Timestamp date = bids_rs.getTimestamp("BidTime");
                            Double amount = bids_rs.getDouble("Amount");
                            bidder.setAttribute("UserID", bidderID);
                            bid.appendChild(bidder);
                            Element time = doc.createElement("Time");
                            time.appendChild(doc.createTextNode(dateToString(date)));
                            bid.appendChild(time);
                            Element amo = doc.createElement("Amount");
                            amo.appendChild(doc.createTextNode("$"+String.format("%.2f", amount)));
                            bid.appendChild(amo);

                            bids.appendChild(bid);
                        }
                        num_bids.appendChild(doc.createTextNode(String.valueOf(num)));
                        item.appendChild(bids);
                    } else if (col.equals("Location")) {
                        Element location = doc.createElement("Location");
                        location.appendChild(doc.createTextNode(item_rs.getString(col)));
                        String Latitude = item_rs.getString("Latitude");
                        String Longitude= item_rs.getString("Longitude");
                        if (Latitude != null && Latitude.length() != 0) {
                            location.setAttribute("Latitude", Latitude);
                        }
                        if (Longitude != null && Longitude.length() != 0) {
                            location.setAttribute("Longitude", Longitude);
                        }
                        item.appendChild(location);
                    } else if (col.equals("Started") || col.equals("Ends")) {
                        Element tmp = doc.createElement(col);
                        tmp.appendChild(doc.createTextNode(dateToString(item_rs.getTimestamp(col))));
                        item.appendChild(tmp);
                    } else if (col.equals("Seller")) {
                        String sellerID = item_rs.getString("SellerID");
                        Element seller = doc.createElement(col);
                        seller_stmt.setString(1, sellerID);
                        ResultSet rs = seller_stmt.executeQuery();
                        if (rs.next()) {
                            seller.setAttribute("Rating", String.valueOf(rs.getInt("Rating")));
                        }
                        seller.setAttribute("UserID", sellerID);
                        item.appendChild(seller);
                    } else {
                        Element tmp = doc.createElement(col);
                        tmp.appendChild(doc.createTextNode(item_rs.getString(col)));
                        item.appendChild(tmp);
                    }
                }
            } else {
                return "";
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StreamResult results = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(doc);
            transformer.transform(source, results);

            conn.close();
            return results.getWriter().toString();
        } catch (Exception e) {
            System.out.println(e);
        }
		return "";
	}

	private String dateToString(Timestamp date) {
        SimpleDateFormat format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        return format.format(date);
    }

	public String echo(String message) {
		return message;
	}

	private IndexSearcher getSearcher() throws IOException {
		if (searcher == null) {
			searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File(indexFolder))));
		}
		return searcher;
	}

	private QueryParser getParser() {
		if (parser == null) {
			parser = new QueryParser("Content", new StandardAnalyzer());
		}
		return parser;
	}

}
