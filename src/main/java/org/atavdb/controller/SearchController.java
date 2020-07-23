package org.atavdb.controller;

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
import org.springframework.web.servlet.ModelAndView;
import org.atavdb.service.DBManager;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author nick
 */
@Controller
public class SearchController {

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
        if (filter.getQueryType().equals(Data.QUERT_TYPE[1])) {
            return new ModelAndView("redirect:/variant/" + query);
        } else if (filter.getQueryType().equals(Data.QUERT_TYPE[2])) {
            return new ModelAndView("redirect:/gene/" + query);
        } else if (filter.getQueryType().equals(Data.QUERT_TYPE[3])) {
            return new ModelAndView("redirect:/region/" + query);
        } else {
            session.setAttribute("error", filter.getError());
        }

        return new ModelAndView("index");
    }

    @GetMapping("/variant/{variant}")
    public ModelAndView variant(@PathVariable String variant, String phenotype, HttpSession session) {
        session.setAttribute("query", variant);
        if (phenotype != null) {
            session.setAttribute("phenotype", phenotype);
        }
        return doSearch(session);
    }

    @GetMapping("/gene/{gene}")
    public ModelAndView gene(@PathVariable String gene, String phenotype, HttpSession session) {
        session.setAttribute("query", gene);
        if (phenotype != null) {
            session.setAttribute("phenotype", phenotype);
        }
        return doSearch(session);
    }

    @GetMapping("/region/{region}")
    public ModelAndView region(@PathVariable String region, String phenotype, HttpSession session) {
        session.setAttribute("query", region);
        if (phenotype != null) {
            session.setAttribute("phenotype", phenotype);
        }
        return doSearch(session);
    }

    private ModelAndView doSearch(HttpSession session) {
        ModelAndView mv = new ModelAndView("index");

        try {
            DBManager.init();

            FilterManager filter = new FilterManager(session);
            SampleManager.init(filter);
            session.setAttribute("sampleCount", SampleManager.getTotalSampleNum(filter));
            session.setAttribute("error", filter.getError());

            if (filter.isQueryValid()) {
                EffectManager.init();

                ArrayList<Variant> variantList = VariantManager.getVariantList(filter, mv);
                mv.addObject("variantList", variantList);

                if (variantList.isEmpty()) {
                    mv.addObject("message", "No results found from search query.");
                    mv.addObject("flankingRegion", filter.getFlankingRegion());
                }
            }
        } catch (Exception ex) {
            // debug purpose
//             mv.addObject("error", convertStackTraceToString(ex));
        }

        return mv;
    }
}
