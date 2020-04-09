package controller;

import util.DBManager;
import model.Output;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Nick
 */
public class Search extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            if (request.getParameter("query") != null) {
                // search by gene or region or variant
                String query = request.getParameter("query").toUpperCase();

                DBManager.init();

                Output.init(query);

                request.setAttribute("query", query);
                request.setAttribute("variant", Output.variant);
                request.setAttribute("errorMsg", Output.errorMsg);
            }

            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (Exception ex) {
            // debug purpose
//            request.setAttribute("error", ex.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void setRequest(HttpServletRequest request) {

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "atavdb search query";
    }
}
