package org.atavdb.controller;

import java.util.ArrayList;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.atavdb.exception.InvalidQueryException;
import org.atavdb.exception.NotFoundException;
import org.atavdb.model.SearchFilter;
import org.atavdb.model.Variant;
import org.atavdb.model.SampleManager;
import org.atavdb.model.VariantManager;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/search")
    public ResponseEntity<Object> search(String query, String maf, String phenotype,
            String isHighQualityVariant, String isUltraRareVariant,
            String isPublicAvailable, HttpSession session, HttpServletResponse response) throws Exception {

        session.setAttribute("query", query);
        session.setAttribute("phenotype", phenotype);
        session.setAttribute("maf", maf);
        session.setAttribute("isHighQualityVariant", isHighQualityVariant);
        session.setAttribute("isUltraRareVariant", isUltraRareVariant);
        session.setAttribute("isPublicAvailable", isPublicAvailable);

        SearchFilter filter = new SearchFilter(session);
        if (filter.isQueryVariant()) {
            return variant(query, phenotype, session);
        } else if (filter.isQueryGene()) {
            return gene(query, phenotype, session);
        } else if (filter.isQueryRegion()) {
            return region(query, phenotype, session);
        }
        
        throw new InvalidQueryException();
    }

    @GetMapping("/variant/{variant}")
    public ResponseEntity<Object> variant(@PathVariable String variant, String phenotype, HttpSession session) throws Exception {
//        if (session.getAttribute("sequence_authorized") == null) {
//            throw new UserAccessException();
//        }

        session.setAttribute("query", variant);
        if (phenotype != null) {
            session.setAttribute("phenotype", phenotype);
        }

        return doSearch(session);
    }

    @GetMapping("/gene/{gene}")
    public ResponseEntity<Object> gene(@PathVariable String gene, String phenotype, HttpSession session) throws Exception {
//        if (session.getAttribute("sequence_authorized") == null) {
//            throw new UserAccessException();
//        }

        session.setAttribute("query", gene);
        if (phenotype != null) {
            session.setAttribute("phenotype", phenotype);
        }

        return doSearch(session);
    }

    @GetMapping("/region/{region}")
    public ResponseEntity<Object> region(@PathVariable String region, String phenotype, HttpSession session) throws Exception {
//        if (session.getAttribute("sequence_authorized") == null) {
//            throw new UserAccessException();
//        }

        session.setAttribute("query", region);
        if (phenotype != null) {
            session.setAttribute("phenotype", phenotype);
        }

        return doSearch(session);
    }

    public ResponseEntity<Object> doSearch(HttpSession session) throws Exception {
        SearchFilter filter = new SearchFilter(session);
        SampleManager.init(filter, session);

        if (filter.isQueryValid()) {
            ModelAndView mv = new ModelAndView("index");
            ArrayList<Variant> variantList = VariantManager.getVariantList(filter, mv);

            if (variantList.isEmpty()) {
                throw new NotFoundException();
            } else {
                return new ResponseEntity<>(variantList, HttpStatus.OK);
            }
        } else {
            throw new InvalidQueryException();
        }
    }
}
