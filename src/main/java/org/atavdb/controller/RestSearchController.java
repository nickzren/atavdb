package org.atavdb.controller;

import java.util.ArrayList;
import javax.servlet.http.HttpSession;
import org.atavdb.exception.InvalidQueryException;
import org.atavdb.exception.NotFoundException;
import org.atavdb.exception.UserAccessException;
import org.atavdb.model.SearchFilter;
import org.atavdb.model.Variant;
import org.atavdb.service.model.SampleManager;
import org.atavdb.service.model.VariantManager;
import org.atavdb.service.util.DBManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
public class RestSearchController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    DBManager dbManager;

    @Autowired
    VariantManager variantManager;

    @Autowired
    SampleManager sampleManager;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @GetMapping("/variant/{variant}")
    public ResponseEntity<Object> variant(@PathVariable String variant, String phenotype, HttpSession session) throws Exception {
        if(session.getAttribute("sequence_authorized") == null) {
            throw new UserAccessException();
        }
        
        session.setAttribute("query", variant);
        if (phenotype != null) {
            session.setAttribute("phenotype", phenotype);
        }

        dbManager.init();

        SearchFilter filter = applicationContext.getBean(SearchFilter.class);
        filter.init(session);
        sampleManager.init(filter, session);

        if (filter.isQueryValid()) {
            ModelAndView mv = new ModelAndView("index");
            ArrayList<Variant> variantList = variantManager.getVariantList(filter, mv);

            if (variantList.isEmpty()) {
                throw new NotFoundException();
            } else {
                return new ResponseEntity<>(variantList.get(0), HttpStatus.OK);
            }
        } else {
            throw new InvalidQueryException();
        }
    }
}
