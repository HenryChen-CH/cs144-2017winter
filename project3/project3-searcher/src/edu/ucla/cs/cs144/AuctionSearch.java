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
                    +region.getLx()+" "+region.getRy()+","
                    +region.getRx()+" "+region.getRy()+","
                    +region.getRx()+" "+region.getLy()+","
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

            item_stmt.setInt(1, id);
            category_stmt.setInt(1, id);
            ResultSet cat_rs = category_stmt.executeQuery();
            ResultSet item_rs = item_stmt.executeQuery();
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
                    } else if (col.equals("Currently") || col.equals("Buy_Price")) {
                        Double price = item_rs.getDouble(col);
                        if (price != null && price != 0) {
                            Element tmp = doc.createElement(col);
                            tmp.appendChild(doc.createTextNode("$" + String.format("%.2f", price)));
                            item.appendChild(tmp);
                        }
                    } else if (col.equals("Number_of_Bids")) {
                        item.appendChild(num_bids);
                    } else if (col.equals("Bids")) {

                    }
//                    } else {
//                        Element tmp = doc.createElement(col);
//                        tmp.appendChild(doc.createTextNode(item_rs.getString(col)));
//                        item.appendChild(tmp);
//                    }
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

            return results.getWriter().toString();
        } catch (Exception e) {
            System.out.println(e);
        }
		return "";
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
