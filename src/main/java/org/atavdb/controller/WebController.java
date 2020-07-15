package org.atavdb.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import org.atavdb.global.Data;
import org.atavdb.service.EffectManager;
import org.atavdb.service.FilterManager;
import org.atavdb.service.SampleManager;
import org.atavdb.model.Variant;
import org.atavdb.service.VariantManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.atavdb.service.DBManager;
import org.atavdb.service.LDAP;
import org.atavdb.service.VerifyUser;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author nick
 */
@Controller
public class WebController {

    @GetMapping("/about")
    public ModelAndView about() {
        ModelAndView mv = new ModelAndView("about");
        return mv;
    }

    @GetMapping("/contact")
    public ModelAndView contact() {
        ModelAndView mv = new ModelAndView("contact");
        return mv;
    }

    @GetMapping("/terms")
    public ModelAndView terms() {
        ModelAndView mv = new ModelAndView("terms");
        return mv;
    }

    @RequestMapping("/signin")
    public ModelAndView signin(String username, String password,
            HttpSession session) {
        clearSession(session);
        ModelAndView mv = new ModelAndView("signin");

        if (username != null && password != null) {

            if (LDAP.isMCAccountValid(username, password)) {
                session.setAttribute("username", username);

                if (VerifyUser.isAuthorizedFromSequence(username)) {
                    session.setAttribute("sequence_authorized", true);
                }

                return new ModelAndView("redirect:/");
            } else {
                mv.addObject("error", "Invalid CUMC MC account username/password.");
            }
        }

        return mv;
    }

    @RequestMapping("/signout")
    public ModelAndView signout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/")
    public ModelAndView index(HttpSession session) {
        clearSession(session);
        ModelAndView mv = new ModelAndView("index");
        try {
            DBManager.init();
            FilterManager filter = new FilterManager(session);
            SampleManager.init(filter);
            session.setAttribute("sampleCount", SampleManager.getTotalSampleNum(filter));

            if (session.getAttribute("genders") == null) {
                session.setAttribute("genders", org.atavdb.global.Enum.Gender.values());
            }

            if (session.getAttribute("ancestries") == null) {
                session.setAttribute("ancestries", org.atavdb.global.Enum.Ancestry.values());
            }
        } catch (Exception ex) {
            // debug purpose
//            mv.addObject("error", convertStackTraceToString(ex));
        }

        return mv;
    }

    private void clearSession(HttpSession session) {
        session.removeAttribute("query");
        session.removeAttribute("queryType");
        session.removeAttribute("maxAF");
        session.removeAttribute("phenotype");
        session.removeAttribute("isHighQualityVariant");
        session.removeAttribute("isUltraRareVariant");
        session.removeAttribute("isPublicAvailable");
        session.removeAttribute("error");
    }

    @GetMapping("/search")
    public ModelAndView search(String query, String maxAF, String phenotype,
            String isHighQualityVariant, String isUltraRareVariant,
            String isPublicAvailable, HttpSession session) {
        session.setAttribute("query", query);
        session.setAttribute("phenotype", phenotype);
        session.setAttribute("maxAF", maxAF);
        session.setAttribute("isHighQualityVariant", isHighQualityVariant);
        session.setAttribute("isUltraRareVariant", isUltraRareVariant);
        session.setAttribute("isPublicAvailable", isPublicAvailable);

        FilterManager filter = new FilterManager(session);
        SampleManager.init(filter);
        session.setAttribute("sampleCount", SampleManager.getTotalSampleNum(filter));

        session.setAttribute("queryType", filter.getQueryType());
        session.setAttribute("error", filter.getError());

        if (filter.getQueryType().equals(Data.QUERT_TYPE[1])) {
            return new ModelAndView("redirect:/variant/" + query);
        } else if (filter.getQueryType().equals(Data.QUERT_TYPE[2])) {
            return new ModelAndView("redirect:/gene/" + query);
        } else if (filter.getQueryType().equals(Data.QUERT_TYPE[3])) {
            return new ModelAndView("redirect:/region/" + query);
        }

        return new ModelAndView("index");
    }

    @GetMapping("/variant/{variant}")
    public ModelAndView variant(@PathVariable String variant, HttpSession session) {
        session.setAttribute("query", variant);
        return doSearch(session);
    }

    @GetMapping("/gene/{gene}")
    public ModelAndView gene(@PathVariable String gene, HttpSession session) {
        session.setAttribute("query", gene);
        return doSearch(session);
    }

    @GetMapping("/region/{region}")
    public ModelAndView region(@PathVariable String region, HttpSession session) {
        session.setAttribute("query", region);
        return doSearch(session);
    }

    private ModelAndView doSearch(HttpSession session) {
        ModelAndView mv = new ModelAndView("index");

        try {
            DBManager.init();

            FilterManager filter = new FilterManager(session);

            if (filter.isQueryValid()) {
                EffectManager.init();

                SampleManager.init(filter);

                ArrayList<Variant> variantList = VariantManager.getVariantList(filter, mv);
                mv.addObject("variantList", variantList);

                if (variantList.isEmpty()) {
                    mv.addObject("message", "No results found from search query.");
                }
            }
        } catch (Exception ex) {
            // debug purpose
//             mv.addObject("error", convertStackTraceToString(ex));
        }

        return mv;
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
}
