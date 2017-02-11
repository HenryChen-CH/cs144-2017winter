package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
        URL url = new URL("http://google.com/complete/search?output=toolbar&q="+request.getAttribute("q"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        PrintWriter out = response.getWriter();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        while ((line = in.readLine()) != null) {
            out.write(line);
        }
        in.close();
        out.close();
    }
}
