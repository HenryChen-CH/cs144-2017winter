/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;


class MyParser {
    
    static final String columnSeparator = "|*|";
    static final String nullSeparator = "\\N";
    static DocumentBuilder builder;
    static BufferedWriter itemsFile, bidsFile, categoriesFile, sellersFile, biddersFile;
    static final String[] itemCols =
            new String[]{"Name", "Currently", "Buy_Price", "First_Bid", "Location", "Country", "Started", "Ends", "Seller","Description"};

    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);
        
        /* Fill in code here (you will probably need to write auxiliary
            methods). */
        StringBuilder itemsSb;
        StringBuilder bidsSb;
        StringBuilder categoriesSb;
        StringBuilder sellersSb;
        StringBuilder biddersSb;
        String itemID;
        try {
            for (Element item : getElementsByTagNameNR(doc.getDocumentElement(), "Item")) {
                itemsSb = new StringBuilder();
                sellersSb = new StringBuilder();
                biddersSb = new StringBuilder();

                // Items
                itemID = item.getAttribute("ItemID");
                itemsSb.append(itemID);
                itemsSb.append(columnSeparator);
                for (int i = 0; i < itemCols.length; i++) {
                    if (itemCols[i].equals("Currently") || itemCols[i].equals("Buy_Price") || itemCols[i].equals("First_Bid")) {
                        String price = getElementTextByTagNameNR(item, itemCols[i]);
                        if (price.length() == 0) {
                            itemsSb.append(nullSeparator);
                        } else {
                            itemsSb.append(strip(price));
                        }
                        itemsSb.append(columnSeparator);
                    } else if (itemCols[i].equals("Location")) {
                        Element location = getElementByTagNameNR(item, itemCols[i]);
                        itemsSb.append(getElementText(location));
                        itemsSb.append(columnSeparator);
                        String tmp_lat = location.getAttribute("Latitude");
                        String tmp_long = location.getAttribute("Longitude");
                        itemsSb.append(tmp_lat.length() == 0 ? nullSeparator : tmp_lat);
                        itemsSb.append(columnSeparator);
                        itemsSb.append(tmp_long.length() == 0 ? nullSeparator : tmp_long);
                        itemsSb.append(columnSeparator);
                    } else if (itemCols[i].equals("Started") || itemCols[i].equals("Ends")) {
                        String time = getElementTextByTagNameNR(item, itemCols[i]);
                        itemsSb.append(timeToMysql(time));
                        itemsSb.append(columnSeparator);
                    } else if (itemCols[i].equals("Seller")) {
                        itemsSb.append(getElementByTagNameNR(item, itemCols[i]).getAttribute("SellerID"));
                        itemsSb.append(columnSeparator);
                    } else if (itemCols[i].equals("Description")) {
                        String description = getElementTextByTagNameNR(item, itemCols[i]);
                        itemsSb.append(description.substring(0, Math.min(4000, description.length())));
                        itemsSb.append("\n");
                    } else {
                        itemsSb.append(getElementTextByTagNameNR(item, itemCols[i]));
                        itemsSb.append(columnSeparator);
                    }
                }
                itemsFile.write(itemsSb.toString());

                // categories
                Element[] categories = getElementsByTagNameNR(item, "Category");
                for (Element category : categories) {
                    categoriesSb = new StringBuilder();
                    categoriesSb.append(itemID);
                    categoriesSb.append(columnSeparator);
                    categoriesSb.append(getElementText(category));
                    categoriesSb.append("\n");
                    categoriesFile.write(categoriesSb.toString());
                }

                //Bids
                Element[] bids = getElementsByTagNameNR(getElementByTagNameNR(item, "Bids"), "Bid");
                for (Element bid : bids) {
                    bidsSb = new StringBuilder();
                    biddersSb = new StringBuilder();
                    Element bidder = getElementByTagNameNR(bid, "Bidder");
                    biddersSb.append(bidder.getAttribute("UserID"));
                    biddersSb.append(columnSeparator);
                    biddersSb.append(bidder.getAttribute("Rating"));
                    biddersSb.append(columnSeparator);
                    biddersSb.append(getElementTextByTagNameNR(bidder, "Location"));
                    biddersSb.append(columnSeparator);
                    biddersSb.append(getElementTextByTagNameNR(bidder, "Country"));
                    biddersSb.append("\n");
                    biddersFile.write(biddersSb.toString());

                    bidsSb.append(itemID);
                    bidsSb.append(columnSeparator);
                    bidsSb.append(bidder.getAttribute("UserID"));
                    bidsSb.append(columnSeparator);
                    bidsSb.append(timeToMysql(getElementTextByTagNameNR(bid, "Time")));
                    bidsSb.append(columnSeparator);
                    bidsSb.append(strip(getElementTextByTagNameNR(bid, "Amount")));
                    bidsSb.append("\n");
                    bidsFile.write(bidsSb.toString());
                }

                // sellers
                Element seller = getElementByTagNameNR(item, "Seller");
                sellersSb.append(seller.getAttribute("UserID"));
                sellersSb.append(columnSeparator);
                sellersSb.append(seller.getAttribute("Rating"));
                sellersSb.append("\n");
                sellersFile.write(sellersSb.toString());


            }
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
        
        /**************************************************************/
        
    }

    private static String timeToMysql(String time) {
        SimpleDateFormat format = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        try {
            Date tmp = format.parse(time);
            format.applyPattern("yyyy-MM-dd HH:mm:ss");
            return format.format(tmp);
        } catch (ParseException e) {
            System.out.println("Parse Exception");
        }
        return "";
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }

        try {
            itemsFile = new BufferedWriter(new FileWriter("Items.csv", false));
            bidsFile = new BufferedWriter(new FileWriter("Bids.csv", false));
            categoriesFile = new BufferedWriter(new FileWriter("Categories.csv", false));
            sellersFile = new BufferedWriter(new FileWriter("Sellers.csv", false));
            biddersFile = new BufferedWriter(new FileWriter("Bidders.csv", false));

            /* Process all files listed on command line. */
            for (int i = 0; i < args.length; i++) {
                File currentFile = new File(args[i]);
                processFile(currentFile);
            }

            itemsFile.close();
            bidsFile.close();
            categoriesFile.close();
            sellersFile.close();
            biddersFile.close();
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }
}
