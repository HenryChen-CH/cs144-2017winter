package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXB;
import java.io.StringReader;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        String xml = AuctionSearch.getXMLDataForItemId(request.getParameter("id"));
        StringReader reader = new StringReader(xml);
        try {
            Item item = JAXB.unmarshal(reader, Item.class);
            item.sortedBids();
            request.setAttribute("item", item);
            HttpSession session = request.getSession(true);
            session.setAttribute("item", item);

            request.getRequestDispatcher("WEB-INF/itemDisplay.jsp").forward(request, response);
        } catch (Exception e) {
            request.getRequestDispatcher("getItem.html").forward(request, response);
        }
    }
}