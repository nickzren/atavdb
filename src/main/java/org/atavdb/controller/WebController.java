package org.atavdb.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import model.EffectManager;
import model.FilterManager;
import model.SampleManager;
import model.Variant;
import model.VariantManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import util.DBManager;
import util.LDAP;
import util.VerifyUser;

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
    public ModelAndView index(
            String query,
            String maxAF,
            String phenotype,
            String isHighQualityVariant,
            String isUltraRareVariant,
            String isPublicAvailable,
            HttpSession session) {
        ModelAndView mv = new ModelAndView("index");
        try {
            DBManager.init();
            FilterManager filter = new FilterManager(
                    query,
                    maxAF,
                    phenotype,
                    isHighQualityVariant,
                    isUltraRareVariant,
                    isPublicAvailable,
                    session,
                    mv);
            SampleManager.init(filter);
            session.setAttribute("sampleCount", SampleManager.getTotalSampleNum(filter));

            if (session.getAttribute("genders") == null) {
                session.setAttribute("genders", global.Enum.Gender.values());
            }
            
            if (session.getAttribute("ethnicities") == null) {
                session.setAttribute("ethnicities", global.Enum.Ethnicity.values());
            }
        } catch (Exception ex) {
            // debug purpose
//            mv.addObject("error", convertStackTraceToString(ex));
        }

        return mv;
    }

    @GetMapping("/search")
    public ModelAndView search(
            String query,
            String maxAF,
            String phenotype,
            String isHighQualityVariant,
            String isUltraRareVariant,
            String isPublicAvailable,
            HttpSession session) {
        ModelAndView mv = new ModelAndView("index");

        try {
            DBManager.init();

            FilterManager filter = new FilterManager(
                    query,
                    maxAF,
                    phenotype,
                    isHighQualityVariant,
                    isUltraRareVariant,
                    isPublicAvailable,
                    session,
                    mv);
            if (filter.isQueryValid()) {
                EffectManager.init();

                SampleManager.init(filter);

                ArrayList<Variant> variantList = VariantManager.getVariantList(filter, mv);

                if (variantList.isEmpty()) {
                    mv.addObject("message", "No results found from search query.");
                }

                mv.addObject("variantList", variantList);
                session.setAttribute("sampleCount", SampleManager.getTotalSampleNum(filter));
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
