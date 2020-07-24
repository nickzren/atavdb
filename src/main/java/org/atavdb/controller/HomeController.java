package org.atavdb.controller;

import javax.servlet.http.HttpSession;
import org.atavdb.model.SearchFilter;
import org.atavdb.service.SampleManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.atavdb.service.DBManager;
import org.atavdb.service.SessionManager;
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
            sampleManager.init(filter);
            session.setAttribute("sampleCount", sampleManager.getTotalSampleNum(filter));

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
