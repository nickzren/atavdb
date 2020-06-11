package controller;

import util.DBManager;
import model.VariantManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Variant;
import model.EffectManager;
import model.SampleManager;
import model.FilterManager;

/**
 *
 * @author Nick
 */
public class Search extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            initSession(session);
            DBManager.init();

            FilterManager filter = new FilterManager(request);
            if (filter.isQueryValid()) {
                EffectManager.init();

                SampleManager.init(filter);

                ArrayList<Variant> variantList = VariantManager.getVariantList(filter, request);

                if (variantList.isEmpty()) {
                    request.setAttribute("message", "No results found from search query.");
                }

                request.setAttribute("variantList", variantList);
            }

            request.getRequestDispatcher("index.jsp").forward(request, response);
        } catch (Exception ex) {
            // debug purpose
//            request.setAttribute("error", convertStackTraceToString(ex));
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }

    private void initSession(HttpSession session) {
        if (session.getAttribute("genders") == null) {
            session.setAttribute("genders", global.Enum.Gender.values());
        }

        if (session.getAttribute("ethnicities") == null) {
            session.setAttribute("ethnicities", global.Enum.Ethnicity.values());
        }
    }

    /**
     * Convert a stack trace to a string for printing or logging including
     * nested exception ("caused by")
     *
     * @param pThrowable
     * @return
     */
    private static String convertStackTraceToString(Throwable pThrowable) {
        if (pThrowable == null) {
            return null;
        } else {
            StringWriter sw = new StringWriter();
            pThrowable.printStackTrace(new PrintWriter(sw));
            return sw.toString();
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
