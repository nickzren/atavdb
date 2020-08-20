package org.atavdb.controller;

import javax.servlet.http.HttpSession;
import org.atavdb.model.SearchFilter;
import org.atavdb.service.model.SampleManager;
import org.atavdb.service.util.ErrorManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.atavdb.service.util.SessionManager;
import org.springframework.stereotype.Controller;

/**
 *
 * @author nick
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView index(HttpSession session) {
        SessionManager.clearSession4Search(session);
        ModelAndView mv = new ModelAndView("index");
        try {
            SearchFilter filter = new SearchFilter(session);
            SampleManager.init(filter, session);

            if (session.getAttribute("genders") == null) {
                session.setAttribute("genders", org.atavdb.global.Enum.Gender.values());
            }

            if (session.getAttribute("ancestries") == null) {
                session.setAttribute("ancestries", org.atavdb.global.Enum.Ancestry.values());
            }

            if (session.getAttribute("maf_list") == null) {
                session.setAttribute("maf_list", SearchFilter.MAF_LIST);
            }

            if (session.getAttribute("phenotype_list") == null) {
                session.setAttribute("phenotype_list", SearchFilter.PHENOTYPE_LIST);
            }
        } catch (Exception ex) {
            session.setAttribute("error", ErrorManager.convertStackTraceToString(ex));
            return new ModelAndView("redirect:/error");
        }

        return mv;
    }

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

    @GetMapping("/error")
    public ModelAndView error() {
        ModelAndView mv = new ModelAndView("error");
        return mv;
    }
}
