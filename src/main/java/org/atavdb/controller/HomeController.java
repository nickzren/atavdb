package org.atavdb.controller;

import javax.servlet.http.HttpSession;
import org.atavdb.service.FilterManager;
import org.atavdb.service.SampleManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.atavdb.service.DBManager;
import org.atavdb.service.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

/**
 *
 * @author nick
 */
@Controller
@ComponentScan("org.atavdb.service")
public class HomeController {
    @Autowired
    SessionManager sessionManager;
    
    @GetMapping("/")
    public ModelAndView index(HttpSession session) {
        sessionManager.clearSession4Search(session);
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
}
