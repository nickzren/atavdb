package org.atavdb.controller;

import javax.servlet.http.HttpSession;
import org.atavdb.model.SearchFilter;
import org.atavdb.service.model.SampleManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.atavdb.service.util.DBManager;
import org.atavdb.service.util.ErrorManager;
import org.atavdb.service.util.SessionManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

/**
 *
 * @author nick
 */
@Controller
@ComponentScan("org.atavdb.service")
@ComponentScan("org.atavdb.model")
public class HomeController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    DBManager dbManager;

    @Autowired
    SessionManager sessionManager;

    @Autowired
    SampleManager sampleManager;

    @Autowired
    ErrorManager errorManager;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @GetMapping("/")
    public ModelAndView index(HttpSession session) {
        sessionManager.clearSession4Search(session);
        ModelAndView mv = new ModelAndView("index");
        try {
            dbManager.init();
            SearchFilter filter = applicationContext.getBean(SearchFilter.class);
            filter.init(session);
            sampleManager.init(filter, session);
            
            if (session.getAttribute("genders") == null) {
                session.setAttribute("genders", org.atavdb.global.Enum.Gender.values());
            }

            if (session.getAttribute("ancestries") == null) {
                session.setAttribute("ancestries", org.atavdb.global.Enum.Ancestry.values());
            }

            if (session.getAttribute("af_list") == null) {
                session.setAttribute("af_list", SearchFilter.AF_LIST);
            }

            if (session.getAttribute("phenotype_list") == null) {
                session.setAttribute("phenotype_list", SearchFilter.PHENOTYPE_LIST);
            }
        } catch (Exception ex) {
            session.setAttribute("error", errorManager.convertStackTraceToString(ex));
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
