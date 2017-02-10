package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        String query = "";
        int numResultsToSkip = 0, numResultsToReturn = 0;

        query = (String)request.getParameter("q");
        numResultsToSkip = Integer.parseInt((String)request.getParameter("numResultsToSkip"));
        numResultsToReturn = Integer.parseInt((String)request.getParameter("numResultsToReturn"));
        SearchResult[] sr = AuctionSearch.basicSearch(query, numResultsToSkip, numResultsToReturn);

        request.setAttribute("results", sr);
        request.getRequestDispatcher("/WEB-INF/keywordSearchResultDisplay.jsp").forward(request, response);

    }
}
