package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuyServlet extends HttpServlet implements Servlet {
    public BuyServlet(){}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(false);
        if (session == null) return;
        request.setAttribute("item", session.getAttribute("item"));
        request.getRequestDispatcher("/WEB-INF/buyPage.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        HttpSession session = request.getSession(false);
        if (session == null) return;
        request.setAttribute("item", session.getAttribute("item"));
        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String currentTime = format.format(date);
        request.setAttribute("time", currentTime);
        request.getRequestDispatcher("/WEB-INF/confirmPage.jsp").forward(request, response);
    }
}
