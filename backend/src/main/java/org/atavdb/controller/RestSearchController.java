package org.atavdb.controller;

import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.atavdb.exception.InvalidSearchException;
import org.atavdb.exception.NotFoundException;
import org.atavdb.model.SearchFilter;
import org.atavdb.model.Variant;
import org.atavdb.model.SampleManager;
import org.atavdb.model.VariantManager;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
public class RestSearchController {

    @GetMapping("/querytype")
    public ResponseEntity<String> querytype(String query, HttpSession session) throws Exception {
        session.setAttribute("query", query);

        SearchFilter filter = new SearchFilter(session);

        if (filter.isQueryValid()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"querytype\":\"" + filter.getQueryType().toLowerCase() + "\"}");

        }

        throw new InvalidSearchException();
    }

    @GetMapping("/search")
    public Collection<Variant> search(String query, String phenotype, String maf,
            String isHighQualityVariant, String isUltraRareVariant,
            String isPublicAvailable, HttpSession session) throws Exception {
        //        if (session.getAttribute("sequence_authorized") == null) {
//            throw new UserAccessException();
//        }

        session.setAttribute("query", query);
        session.setAttribute("phenotype", phenotype);
        session.setAttribute("maf", maf);
        session.setAttribute("isHighQualityVariant", isHighQualityVariant);
        session.setAttribute("isUltraRareVariant", isUltraRareVariant);
        session.setAttribute("isPublicAvailable", isPublicAvailable);

        SearchFilter filter = new SearchFilter(session);
        SampleManager.init(filter, session);

        if (filter.isQueryValid()) {
            ModelAndView mv = new ModelAndView("index");
            ArrayList<Variant> variantList = VariantManager.getVariantList(filter, mv);

            if (variantList.isEmpty()) {
                throw new NotFoundException();
            } else {
                return variantList;
            }
        } else {
            throw new InvalidSearchException();
        }
    }

    @GetMapping("/variant/{variant}")
    public Collection<Variant> variant(@PathVariable String variant, String phenotype, String maf,
            String isHighQualityVariant, String isUltraRareVariant,
            String isPublicAvailable, HttpSession session) throws Exception {
        return search(variant, phenotype, maf, isHighQualityVariant, isUltraRareVariant, isPublicAvailable, session);
    }

    @GetMapping("/gene/{gene}")
    public Collection<Variant> gene(@PathVariable String gene, String phenotype, String maf,
            String isHighQualityVariant, String isUltraRareVariant,
            String isPublicAvailable, HttpSession session) throws Exception {
        return search(gene, phenotype, maf, isHighQualityVariant, isUltraRareVariant, isPublicAvailable, session);
    }

    @GetMapping("/region/{region}")
    public Collection<Variant> region(@PathVariable String region, String phenotype, String maf,
            String isHighQualityVariant, String isUltraRareVariant,
            String isPublicAvailable, HttpSession session) throws Exception {
        return search(region, phenotype, maf, isHighQualityVariant, isUltraRareVariant, isPublicAvailable, session);
    }
}
