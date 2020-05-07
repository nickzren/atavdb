package controller;

import global.Data;
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
import model.GeneManager;
import model.RegionManager;
import model.SampleManager;
import model.Filter;

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
                    Filter filter = new Filter(request);
                    
                    DBManager.init();

                    EffectManager.init();

                    SampleManager.init(filter);

                    RegionManager.init();

                    String queryType = getQueryType(query);

                    ArrayList<CalledVariant> variantList = VariantManager.getVariantList(query, queryType, filter);

                    request.setAttribute("query", query);
                    request.setAttribute("queryType", queryType);
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

    private String getQueryType(String query) throws Exception {
        if (query.split("-").length == 4) {
            String[] tmp = query.split("-");
            if (RegionManager.isChrValid(tmp[0]) // check valid chr 
                    && tmp[1].matches("^[1-9]\\d*$") // check valid positive integer
                    && tmp[2].matches("^[ATCG]+$") // valid if only contains ATCG 
                    && tmp[3].matches("^[ATCG]+$") // valid if only contains ATCG 
                    ) {
                return Data.QUERT_TYPE[1]; // Variant
            }
        } else if (query.contains(":")) {
            String[] tmp = query.split(":");
            if (tmp.length == 2
                    && RegionManager.isChrValid(tmp[0])) { // check valid chr 
                tmp = tmp[1].split("-");
                if (tmp.length == 2
                        && tmp[0].matches("^[1-9]\\d*$") // check valid positive integer
                        && tmp[1].matches("^[1-9]\\d*$") // check valid positive integer
                        ) {
                    int end = Integer.valueOf(tmp[1]);
                    int start = Integer.valueOf(tmp[0]);

                    if (end >= start 
                            && (end - start) <= RegionManager.MAX_SEARCH_LIMIT) {
                        return Data.QUERT_TYPE[3]; // Region
                    }
                }
            }
        } else if (GeneManager.isValid(query)) {
            return Data.QUERT_TYPE[2]; // Gene
        }

        return Data.QUERT_TYPE[0]; // Invalid
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
