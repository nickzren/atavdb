package org.atavdb.controller;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.atavdb.exception.InvalidSearchException;
import org.atavdb.exception.NotFoundException;
import org.atavdb.exception.TooManyRequestException;
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

    // API rate limit cache bucket map - key: query type + client's IP
    private final Map<String, Bucket> cacheBucket = new ConcurrentHashMap<>();

    // API daily rate limit capacity per client's IP
    private static final int VARIANT_API_CAPACITY = 1000;
    private static final int GENE_API_CAPACITY = 50;
    private static final int REGION_API_CAPACITY = 100;

    @GetMapping("/querytype")
    public ResponseEntity<String> querytype(String query) throws Exception {
        SearchFilter filter = new SearchFilter(query, null, null, null, null, null);

        if (filter.isQueryValid()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"querytype\":\"" + filter.getQueryType().toLowerCase() + "\"}");
        }

        throw new InvalidSearchException();
    }

    @GetMapping("/search")
    public Collection<Variant> search(
            String query,
            String phenotype,
            String maf,
            String isHighQualityVariant,
            String isUltraRareVariant,
            String isPubliclyAvailable,
            HttpSession session) throws Exception {
        SearchFilter filter = new SearchFilter(query, phenotype, maf,
                isHighQualityVariant, isUltraRareVariant, isPubliclyAvailable);

        // authorized users can view expriment id
        if (session.getAttribute("authorizedUser") != null) {
            filter.setIsAuthorized(true);
        }

        // anonymous/unauthenticated users restricted to public only data 
        if (session.getAttribute("authenticatedUser") == null) {
            filter.setIsAvailableControlUseOnly(true);
        }

        SampleManager.init(filter);

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
            String isHighQualityVariant, String isUltraRareVariant, String isPubliclyAvailable,
            HttpSession session, HttpServletRequest request) throws Exception {
        String id = "variant" + request.getRemoteAddr();

        if (checkLimit(id, VARIANT_API_CAPACITY)) {
            return search(variant, phenotype, maf, isHighQualityVariant, isUltraRareVariant, isPubliclyAvailable, session);
        } else {
            throw new TooManyRequestException();
        }
    }

    @GetMapping("/gene/{gene}")
    public Collection<Variant> gene(@PathVariable String gene, String phenotype, String maf,
            String isHighQualityVariant, String isUltraRareVariant, String isPubliclyAvailable,
            HttpSession session, HttpServletRequest request) throws Exception {
        String id = "gene" + request.getRemoteAddr();

        if (checkLimit(id, GENE_API_CAPACITY)) {
            return search(gene, phenotype, maf, isHighQualityVariant, isUltraRareVariant, isPubliclyAvailable, session);
        } else {
            throw new TooManyRequestException();
        }
    }

    @GetMapping("/region/{region}")
    public Collection<Variant> region(@PathVariable String region, String phenotype, String maf,
            String isHighQualityVariant, String isUltraRareVariant, String isPubliclyAvailable,
            HttpSession session, HttpServletRequest request) throws Exception {
        String id = "region" + request.getRemoteAddr();

        if (checkLimit(id, REGION_API_CAPACITY)) {
            return search(region, phenotype, maf, isHighQualityVariant, isUltraRareVariant, isPubliclyAvailable, session);
        } else {
            throw new TooManyRequestException();
        }
    }

    private boolean checkLimit(String id, int capacity) {
        Bucket bucket = cacheBucket.get(id);
        if (bucket == null) {
            bucket = Bucket4j.builder().addLimit(
                    Bandwidth.classic(capacity, Refill.intervally(capacity, Duration.ofDays(1)))
            ).build();
            cacheBucket.put(id, bucket);
        }

        return bucket.tryConsume(1);
    }
}
