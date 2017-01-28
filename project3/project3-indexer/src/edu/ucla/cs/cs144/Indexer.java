package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.*;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer {

    /**
     * Creates a new instance of Indexer
     */

    public Indexer() {
    }

    public void rebuildIndexes() {

        // create a connection to the database to retrieve Items from MySQL
        Connection conn;
        IndexWriter indexWriter;

        try {
            conn = DbManager.getConnection(true);
            Directory indexDir = FSDirectory.open(new File("/var/lib/lucene/index1/"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_2, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
        } catch (SQLException ex) {
            System.out.println(ex);
            return;
        } catch (IOException e) {
            System.out.println(e);
            return;
        }


	/*
     * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add
         * new methods and create additional Java classes.
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 *
	 */
        try {
            Statement stmt = conn.createStatement();
            PreparedStatement p_stmt = conn.prepareStatement("select Category from Categories where ItemID=?");
            ResultSet rs = stmt.executeQuery("select ItemID, Name, Description from Items");
            int id;
            String name, description;

            while (rs.next()) {
                id = rs.getInt("ItemID");
                Document doc = new Document();
                name = rs.getString("Name");
                description = rs.getString("Description");
                doc.add(new IntField("ItemID", id, Field.Store.YES));
                doc.add(new StringField("Name", name, Field.Store.YES));
                doc.add(new StringField("Description", description, Field.Store.NO));

                p_stmt.setInt(1, id);
                ResultSet crs = p_stmt.executeQuery();
                StringBuilder sb = new StringBuilder();
                while (crs.next()) {
                    sb.append(crs.getString(1));
                    sb.append(" ");
                }
                sb.deleteCharAt(sb.length()-1);
                String category = sb.toString();
                doc.add(new TextField("Category", category, Field.Store.NO));
                doc.add(new TextField("Content", name+" "+category+" "+description, Field.Store.NO));
                indexWriter.addDocument(doc);
            }
            indexWriter.close();
        } catch (SQLException e) {
            System.out.println("SQL Exception\n");
            return;
        } catch (IOException e) {
            System.out.println(e);
            return;
        }


        // close the database connection
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
            return;
        }
    }

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        idx.rebuildIndexes();
    }
}