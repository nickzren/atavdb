package org.atavdb.controller;

import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import org.atavdb.model.SearchFilter;
import org.atavdb.model.SampleManager;
import org.atavdb.model.Variant;
import org.atavdb.model.VariantManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.atavdb.util.ErrorManager;
import org.springframework.web.bind.annotation.PathVariable;

/**
 *
 * @author nick
 */
@Controller
public class SearchController {

    @GetMapping("/search")
    public ModelAndView search(String query, String maf, String phenotype,
            String isHighQualityVariant, String isUltraRareVariant,
            String isPublicAvailable, HttpSession session) {
        try {
            session.setAttribute("query", query);
            session.setAttribute("phenotype", phenotype);
            session.setAttribute("maf", maf);
            session.setAttribute("isHighQualityVariant", isHighQualityVariant);
            session.setAttribute("isUltraRareVariant", isUltraRareVariant);
            session.setAttribute("isPublicAvailable", isPublicAvailable);

            SearchFilter filter = new SearchFilter(session);
            if (filter.isQueryVariant()) {
                return new ModelAndView("redirect:/variant/" + query);
            } else if (filter.isQueryGene()) {
                return new ModelAndView("redirect:/gene/" + query);
            } else if (filter.isQueryRegion()) {
                return new ModelAndView("redirect:/region/" + query);
            } else {
//                session.setAttribute("error", filter.getError());
            }
        } catch (Exception ex) {
            session.setAttribute("error", ErrorManager.convertStackTraceToString(ex));
            return new ModelAndView("redirect:/error");
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
            SearchFilter filter = new SearchFilter(session);
            SampleManager.init(filter, session);

            if (filter.isQueryValid()) {
                ArrayList<Variant> variantList = VariantManager.getVariantList(filter, mv);
                mv.addObject("variantList", variantList);

                if (variantList.isEmpty()) {
                    mv.addObject("message", "No results found from search query.");
                    mv.addObject("flankingRegion", filter.getFlankingRegion());
                }
            }
        } catch (Exception ex) {
            session.setAttribute("error", ErrorManager.convertStackTraceToString(ex));
            return new ModelAndView("redirect:/error");
        }

        return mv;
    }
}
