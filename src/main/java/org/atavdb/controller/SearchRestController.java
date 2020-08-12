package org.atavdb.controller;

import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import org.atavdb.model.SearchFilter;
import org.atavdb.model.Variant;
import org.atavdb.service.model.SampleManager;
import org.atavdb.service.model.VariantManager;
import org.atavdb.service.util.DBManager;
import org.atavdb.service.util.ErrorManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author nick
 */
@RestController
@RequestMapping("/api")
public class SearchRestController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    DBManager dbManager;

    @Autowired
    VariantManager variantManager;

    @Autowired
    SampleManager sampleManager;

    @Autowired
    ErrorManager errorManager;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @GetMapping("/variant/{variant}")
    public Variant variant(@PathVariable String variant, String phenotype, HttpSession session) {
        session.setAttribute("query", variant);
        if (phenotype != null) {
            session.setAttribute("phenotype", phenotype);
        }

        try {
            dbManager.init();

            SearchFilter filter = applicationContext.getBean(SearchFilter.class);
            filter.init(session);
            sampleManager.init(filter);
            session.setAttribute("sampleCount", sampleManager.getTotalSampleNum(filter));
            session.setAttribute("error", filter.getError());

            if (filter.isQueryValid()) {
                ModelAndView mv = new ModelAndView("index");
                ArrayList<Variant> variantList = variantManager.getVariantList(filter, mv);
//                mv.addObject("variantList", variantList);

//                if (variantList.isEmpty()) {
//                    mv.addObject("message", "No results found from search query.");
//                    mv.addObject("flankingRegion", filter.getFlankingRegion());
//                }

                return variantList.get(0);
            }
        } catch (Exception ex) {
//            session.setAttribute("error", errorManager.convertStackTraceToString(ex));
//            return new ModelAndView("redirect:/error");
        }
        
        return null;
    }
}
