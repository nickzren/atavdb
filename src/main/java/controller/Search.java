package controller;

import util.DBManager;
import model.VariantManager;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.CalledVariant;
import model.EffectManager;
import model.SampleManager;

/**
 *
 * @author Nick
 */
public class Search extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            if (session.getAttribute("username") != null) {
                String query = request.getParameter("query");
                if (query != null && !query.isEmpty()) {
                    DBManager.init();

                    EffectManager.init();

                    SampleManager.init();

                    ArrayList<CalledVariant> variantList = VariantManager.getVariantList(query);

                    request.setAttribute("query", query);
                    request.setAttribute("variantList", variantList);
                }

                request.getRequestDispatcher("index.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("signin.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            // debug purpose
//            request.setAttribute("error", ex.toString());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
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
