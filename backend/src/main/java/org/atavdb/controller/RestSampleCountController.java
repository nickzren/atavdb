package org.atavdb.controller;

import org.atavdb.model.SampleManager;
import org.atavdb.model.SearchFilter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nick
 */
@RestController
@RequestMapping("/api")
public class RestSampleCountController {

    @GetMapping("/sample-count")
    public ResponseEntity<String> index(String phenotype,String isPubliclyAvailable) throws Exception {
        SearchFilter filter = new SearchFilter(null, phenotype, null, null, null, isPubliclyAvailable, null);
        SampleManager.init(filter);
        int sampleCount = SampleManager.getTotalSampleNum(filter);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"sampleCount\":\"" + String.valueOf(sampleCount) + "\"}");
    }
}
